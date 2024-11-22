package io.micronaut.guides

import com.networknt.schema.JsonSchema
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.Memoized
import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.guides.core.App
import io.micronaut.guides.core.CopyFileVisitor
import io.micronaut.guides.core.Guide
import io.micronaut.guides.core.GuideParser
import io.micronaut.guides.core.GuideUtils
import io.micronaut.guides.core.GuidesOption
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import org.gradle.api.GradleException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.regex.Pattern

import static groovy.io.FileType.FILES
import static io.micronaut.core.util.StringUtils.EMPTY_STRING
import static io.micronaut.starter.api.TestFramework.JUNIT
import static io.micronaut.starter.api.TestFramework.SPOCK
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
    private static final List<JdkVersion> JDK_VERSIONS_SUPPORTED_BY_GRAALVM = [JDK_21]
    public static final String LICENSEHEADER = "LICENSEHEADER"

    private final ApplicationContext applicationContext
    private final GuidesGenerator guidesGenerator
    private final GuideParser guideParser;

    GuideProjectGenerator() {
        applicationContext = ApplicationContext.run()
        guidesGenerator = applicationContext.getBean(GuidesGenerator)
        guideParser = applicationContext.getBean(GuideParser)
    }

    @Override
    void close() {
        applicationContext.close()
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

        List<Guide> metadatas = guideParser.parseGuidesMetadata(guidesDir, metadataConfigName)
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

                // typical guides use 'default' as name, multi-project guides have different modules
                String folder = folderName(metadata.slug(), guidesOption)

                String appName = app.name() == DEFAULT_APP_NAME ? EMPTY_STRING : app.name()


                Path destinationPath = Paths.get(outputDir.absolutePath, folder, appName)
                File destination = destinationPath.toFile()
                destination.mkdir()

                String packageAndName = BASE_PACKAGE + '.' + app.name()

                guidesGenerator.generateAppIntoDirectory(destination, app.applicationType(), packageAndName, app.framework(),
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

        for (BuildTool buildTool : buildTools) {
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
