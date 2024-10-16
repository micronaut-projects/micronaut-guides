package io.micronaut.guides

import com.networknt.schema.InputFormat
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SchemaLocation
import com.networknt.schema.SchemaValidatorsConfig
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.Memoized
import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.io.IOUtils
import io.micronaut.core.io.ResourceLoader
import io.micronaut.guides.core.App
import io.micronaut.guides.core.Guide
import io.micronaut.guides.core.GuideUtils
import io.micronaut.json.JsonMapper
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.api.TestFramework
import org.gradle.api.GradleException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.regex.Pattern
import java.util.stream.Collectors

import static groovy.io.FileType.FILES
import static io.micronaut.core.util.StringUtils.EMPTY_STRING
import static io.micronaut.starter.api.TestFramework.JUNIT
import static io.micronaut.starter.api.TestFramework.SPOCK
import static io.micronaut.starter.options.JdkVersion.JDK_17
import static io.micronaut.starter.options.JdkVersion.JDK_21
import static io.micronaut.starter.options.Language.GROOVY
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

@CompileStatic
class GuideProjectGenerator implements AutoCloseable {
    private static final String EXTENSION_JAVA = ".java"
    private static final String EXTENSION_GROOVY = ".groovy"
    private static final String EXTENSION_KT = ".kt"

    public static final String DEFAULT_APP_NAME = 'default'

    private static final Pattern GROOVY_JAVA_OR_KOTLIN = ~/.*\.java|.*\.groovy|.*\.kt/
    private static final Logger LOG = LoggerFactory.getLogger(this)
    private static final String APP_NAME = 'micronautguide'
    private static final String BASE_PACKAGE = 'example.micronaut'
    private static final List<JdkVersion> JDK_VERSIONS_SUPPORTED_BY_GRAALVM = [JDK_17, JDK_21]
    public static final String LICENSEHEADER = "LICENSEHEADER"

    private final ApplicationContext applicationContext
    private final GuidesGenerator guidesGenerator

    private static JsonMapper jsonMapper;
    private static ResourceLoader resourceLoader;

    static {
        ApplicationContext context = ApplicationContext.run();
        jsonMapper = context.getBean(JsonMapper.class);
        resourceLoader = context.getBean(ResourceLoader.class);
    }

    GuideProjectGenerator() {
        applicationContext = ApplicationContext.run()
        guidesGenerator = applicationContext.getBean(GuidesGenerator)
    }

    @Override
    void close() {
        applicationContext.close()
    }

    @CompileDynamic
    static List<Guide> parseGuidesMetadata(File guidesDir,
                                           String metadataConfigName) {
        List<Guide> metadatas = []

        guidesDir.eachDir { dir ->
            parseGuideMetadata(dir, metadataConfigName).ifPresent(metadatas::add)
        }

        mergeMetadataList(metadatas)

        metadatas
    }

    @CompileDynamic
    static Optional<Guide> parseGuideMetadata(File dir, String metadataConfigName) {
        File configFile = new File(dir, metadataConfigName)
        if (!configFile.exists()) {
            LOG.warn('metadata file not found for {}', dir.name)
            return Optional.empty()
        }


        String content = Files.readString(Paths.get(configFile.toString()))
        Map config = new JsonSlurper().parse(configFile) as Map
        boolean publish = config.publish == null ? true : config.publish

        if (publish) {
            JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012, builder ->
                    // This creates a mapping from $id which starts with https://www.example.org/ to the retrieval URI classpath:schema/
                    builder.schemaMappers(schemaMappers -> schemaMappers.mapPrefix("https://www.guides.micronaut.io/schemas", "classpath:META-INF/schemas"))
            );

            SchemaValidatorsConfig.Builder builder = SchemaValidatorsConfig.builder();
            SchemaValidatorsConfig validatorsConfig = builder.build();
            JsonSchema schema = jsonSchemaFactory.getSchema(SchemaLocation.of("https://www.guides.micronaut.io/schemas/guide-metadata.schema.json"), validatorsConfig);


            Set<ValidationMessage> assertions = schema.validate(content, InputFormat.JSON);

            if (!assertions.isEmpty()) {
                throw new Exception("Guide metadata " + configFile + 'does not validate the JSON Schema\n' + assertions)
            }
        }

        Guide raw = jsonMapper.readValue(content, Guide.class)

        List<App> apps = new LinkedList<>()

        for(App app: raw.apps()){
            apps.add(new App(
                    app.name(),
                    app.packageName(),
                    app.applicationType(),
                    app.framework(),
                    app.features() ?: [],
                    app.invisibleFeatures() ?: [],
                    app.kotlinFeatures() ?: [],
                    app.javaFeatures() ?: [],
                    app.groovyFeatures() ?: [],
                    app.testFramework(),
                    app.excludeTest(),
                    app.excludeSource(),
                    app.validateLicense()
            ))
        }

        return Optional.ofNullable(new Guide(
                raw.title(),
                raw.intro(),
                raw.authors(),
                raw.categories(),
                publish ? raw.publicationDate() : null,
                raw.minimumJavaVersion(),
                raw.maximumJavaVersion(),
                raw.cloud(),
                raw.skipGradleTests(),
                raw.skipMavenTests(),
                publish ? dir.name + '.adoc' : null,
                raw.languages() ?: ['java', 'groovy', 'kotlin'],
                raw.tags(),
                raw.buildTools() ?: ['gradle', 'maven'],
                raw.testFramework(),
                raw.zipIncludes() ?: [],
                dir.name,
                publish,
                raw.base(),
                raw.env() ?: [:],
                apps
        ))
    }

    @CompileDynamic
    void generate(File guidesDir,
                  File output,
                  String metadataConfigName,
                  File projectDir) {

        if (!output.exists()) {
            assert output.mkdir()
        }

        File asciidocDir = new File(projectDir, 'src/docs/asciidoc')
        if (!asciidocDir.exists()) {
            asciidocDir.mkdir()
        }

        List<Guide> metadatas = parseGuidesMetadata(guidesDir, metadataConfigName)
        for (Guide metadata : metadatas) {
            File dir = new File(guidesDir, metadata.slug)
            try {
                if (Utils.process(metadata, false)) {
                    println "Generating projects for $metadata.slug"
                    generateOne(metadata, dir, output)
                    GuideAsciidocGenerator.generate(metadata, dir, asciidocDir, projectDir)
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace()
            }
        }
    }

    static String folderName(String slug, GuidesOption guidesOption) {
        "${slug}-${guidesOption.buildTool}-${guidesOption.language}"
    }

    void generateOne(Guide metadata, File inputDir, File outputDir) {
        if (!outputDir.exists()) {
            assert outputDir.mkdir()
        }

        JdkVersion javaVersion = Utils.parseJdkVersion()
        if (metadata.minimumJavaVersion() != null) {
            JdkVersion minimumJavaVersion = JdkVersion.valueOf(metadata.minimumJavaVersion())
            if (minimumJavaVersion.majorVersion() > javaVersion.majorVersion()) {
                javaVersion = minimumJavaVersion
            }
        }

        if (metadata.maximumJavaVersion() != null && javaVersion.majorVersion() > metadata.maximumJavaVersion()) {
            println "not generating project for ${metadata.slug()}, JDK ${javaVersion.majorVersion()} > ${metadata.maximumJavaVersion()}"
            return
        }

        List<GuidesOption> guidesOptionList = guidesOptions(metadata)
        for (GuidesOption guidesOption : guidesOptionList) {
            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language

            for (App app : metadata.apps()) {
                List<String> appFeatures = ([] as List<String>) + GuideUtils.getAppFeatures(app,lang)
                if (guidesOption.language == GROOVY ||
                        !JDK_VERSIONS_SUPPORTED_BY_GRAALVM.contains(javaVersion)) {
                    appFeatures.remove('graalvm')
                }

                if (testFramework == SPOCK) {
                    appFeatures.remove('mockito')
                }

                // typical guides use 'default' as name, multi-project guides have different modules
                String folder = folderName(metadata.slug(), guidesOption)

                String appName = app.name() == DEFAULT_APP_NAME ? EMPTY_STRING : app.name()


                Path destinationPath = Paths.get(outputDir.absolutePath, folder, appName)
                File destination = destinationPath.toFile()
                destination.mkdir()

                String packageAndName = BASE_PACKAGE + '.' + app.name()

                guidesGenerator.generateAppIntoDirectory(destination, ApplicationType.DEFAULT, packageAndName, app.framework(),
                        appFeatures, buildTool, app.testFramework() ?: testFramework, lang, javaVersion)

                if (metadata.base()) {
                    File baseDir = new File(inputDir.parentFile, metadata.base())
                    copyGuideSourceFiles(baseDir, destinationPath, appName, guidesOption.language.toString(), true)
                }

                copyGuideSourceFiles(inputDir, destinationPath, appName, guidesOption.language.toString())

                if (app.excludeSource()) {
                    for (String mainSource : app.excludeSource()) {
                        File f = fileToDelete(destination, GuideAsciidocGenerator.mainPath(appName, mainSource), guidesOption)
                        if (f.exists()) {
                            f.delete()
                        }
                        f = fileToDelete(destination, GuideAsciidocGenerator.mainPath(EMPTY_STRING, mainSource), guidesOption)
                        if (f.exists()) {
                            f.delete()
                        }
                    }
                }

                if (app.excludeTest()) {
                    for (String testSource : app.excludeTest()) {
                        File f = fileToDelete(destination, GuideAsciidocGenerator.testPath(appName, testSource, testFramework), guidesOption)
                        if (f.exists()) {
                            f.delete()
                        }
                        f = fileToDelete(destination, GuideAsciidocGenerator.testPath(EMPTY_STRING, testSource, testFramework), guidesOption)
                        if (f.exists()) {
                            f.delete()
                        }
                    }
                }

                if (metadata.zipIncludes()) {
                    File destinationRoot = new File(outputDir.absolutePath, folder)
                    for (String zipInclude : metadata.zipIncludes()) {
                        copyFile(inputDir, destinationRoot, zipInclude)
                    }
                }
                addLicenses(new File(outputDir.absolutePath, folder))
            }
        }
    }

    void addLicenses(File folder) {
        String licenseHeader = licenseHeaderText()
        folder.eachFileRecurse (FILES) { file ->
            if (
                    (file.path.endsWith(EXTENSION_JAVA) || file.path.endsWith(EXTENSION_GROOVY) || file.path.endsWith(EXTENSION_KT))
                    && !file.text.contains("Licensed under")
            ) {
                file.text = licenseHeader + file.text
            }
        }
    }

    @Memoized
    private static String licenseHeaderText() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(LICENSEHEADER)
        resource.text.replace('$YEAR', "${LocalDate.now().year}")
    }

    private static void copyGuideSourceFiles(File inputDir, Path destinationPath,
                                             String appName, String language,
                                             boolean ignoreMissingDirectories = false) {

        // look for a common 'src' directory shared by multiple languages and copy those files first
        final String srcFolder = 'src'
        Path srcPath = Paths.get(inputDir.absolutePath, appName, srcFolder)
        if (Files.exists(srcPath)) {
            Files.walkFileTree(srcPath, new CopyFileVisitor(Paths.get(destinationPath.toString(), srcFolder)))
        }

        Path sourcePath = Paths.get(inputDir.absolutePath, appName, language)
        if (!Files.exists(sourcePath)) {
            sourcePath.toFile().mkdir()
        }
        if (Files.exists(sourcePath)) {
            // copy source/resource files for the current language
            Files.walkFileTree(sourcePath, new CopyFileVisitor(destinationPath))
        } else if (!ignoreMissingDirectories) {
            throw new GradleException("source directory ${sourcePath.toFile().absolutePath} does not exist")
        }
    }

    private static File fileToDelete(File destination, String path, GuidesOption guidesOption) {
        Paths.get(destination.absolutePath, path
                .replace("@lang@", guidesOption.language.toString())
                .replace("@languageextension@", guidesOption.language.extension))
                .toFile()
    }

    private static void deleteFile(File destination, String path, GuidesOption guidesOption) {
        fileToDelete(destination, path, guidesOption).delete()
    }

    private static void copyFile(File inputDir, File destinationRoot, String filePath) {
        File sourceFile = new File(inputDir, filePath)
        File destinationFile = new File(destinationRoot, filePath)

        File destinationFileDir = destinationFile.getParentFile()
        if (!destinationFileDir.exists()) {
            Files.createDirectories destinationFileDir.toPath()
        }

        Files.copy sourceFile.toPath(), destinationFile.toPath(), REPLACE_EXISTING
    }

    static List<GuidesOption> guidesOptions(Guide guideMetadata) {
        List<BuildTool> buildTools = guideMetadata.buildTools()
        List<Language> languages = guideMetadata.languages()
        TestFramework testFramework = guideMetadata.testFramework()
        List<GuidesOption> guidesOptionList = []

        for (BuildTool buildTool : BuildTool.values()) {
            for (Language language : Language.values()) {
                if (GuideUtils.shouldSkip(guideMetadata,buildTool)) {
                    LOG.info("Skipping index guide for $buildTool and $language")
                    continue
                }
                if (languages.contains(language)) {
                    guidesOptionList << createGuidesOption(buildTool, language, testFramework)
                }
            }

        }

        guidesOptionList
    }

    private static GuidesOption createGuidesOption(@NonNull BuildTool buildTool,
                                                   @NonNull Language language,
                                                   @Nullable TestFramework testFramework) {
        new GuidesOption(buildTool, language, testFrameworkOption(language, testFramework))
    }

    private static TestFramework testFrameworkOption(@NonNull Language language,
                                                     @Nullable TestFramework testFramework) {
        if (testFramework != null) {
            return testFramework
        }
        if (language == GROOVY) {
            return SPOCK
        }
        JUNIT
    }

    static void mergeMetadataList(List<Guide> metadatas) {
        Map<String, Guide> metadatasByDirectory = new TreeMap<>()
        for (Guide metadata : metadatas) {
            metadatasByDirectory[metadata.slug()] = metadata
        }

        mergeMetadataMap(metadatasByDirectory)

        metadatas.clear()
        metadatas.addAll metadatasByDirectory.values()
    }

    private static void mergeMetadataMap(Map<String, Guide> metadatasByDirectory) {
        for (String dir : [] + metadatasByDirectory.keySet()) {
            Guide metadata = metadatasByDirectory[dir]
            if (metadata.base()) {
                Guide base = metadatasByDirectory[metadata.base()]
                Guide merged = GuideUtils.merge(base, metadata)
                metadatasByDirectory[dir] = merged
            }
        }
    }

    static void deleteEveryFileButSources(File dir) {
        dir.eachFileRecurse(FILES) { file ->
            if (!(file ==~ GROOVY_JAVA_OR_KOTLIN)) {
                file.delete()
            }
        }
        deleteEmptySubDirectories(dir)
    }

    static void deleteEmptySubDirectories(File dir) {
        for (; ;) {
            if (deleteSubDirectories(dir) == 0) {
                break
            }
        }
    }

    static int deleteSubDirectories(File dir) {
        int deleted = 0

        List<File> emptyDirs = []
        dir.eachDirRecurse { f ->
            if (f.listFiles().length == 0) {
                emptyDirs << f
            }
        }
        emptyDirs.each { f ->
            if (f.delete()) {
                deleted++
            }
        }
        deleted
    }
}
