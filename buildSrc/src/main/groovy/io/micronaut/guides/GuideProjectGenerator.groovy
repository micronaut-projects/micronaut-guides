package io.micronaut.guides

import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.guides.GuideMetadata.App
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import org.gradle.api.GradleException

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate

import static io.micronaut.core.util.StringUtils.EMPTY_STRING
import static io.micronaut.starter.api.TestFramework.JUNIT
import static io.micronaut.starter.api.TestFramework.SPOCK
import static io.micronaut.starter.options.JdkVersion.JDK_11
import static io.micronaut.starter.options.JdkVersion.JDK_8
import static io.micronaut.starter.options.Language.GROOVY
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

@CompileStatic
class GuideProjectGenerator implements AutoCloseable {

    public static final String DEFAULT_APP_NAME = 'default'

    private static final String APP_NAME = 'micronautguide'
    private static final String BASE_PACKAGE = 'example.micronaut'
    private static final List<JdkVersion> JDK_VERSIONS_SUPPORTED_BY_GRAALVM = [JDK_8, JDK_11]

    private final ApplicationContext applicationContext
    private final GuidesGenerator guidesGenerator

    GuideProjectGenerator() {
        applicationContext = ApplicationContext.run()
        guidesGenerator = applicationContext.getBean(GuidesGenerator)
    }

    @Override
    void close() {
        applicationContext.close()
    }

    @CompileDynamic
    static List<GuideMetadata> parseGuidesMetadata(File guidesFolder,
                                                   String metadataConfigName) {
        List<GuideMetadata> result = []
        guidesFolder.eachDir { dir ->
            result << parseGuideMetadata(dir, metadataConfigName)
        }
        result
    }

    @CompileDynamic
    private static GuideMetadata parseGuideMetadata(File dir, String metadataConfigName) {
        File configFile = new File(dir, metadataConfigName)
        if (!configFile.exists()) {
            throw new GradleException("metadata file not found for " + dir.name)
        }

        def config = new JsonSlurper().parse(configFile)
        boolean publish = config.publish == null ? true : config.publish

        Category cat = Category.values().find {it.toString() == config.category }
        if (publish && !cat) {
            throw new GradleException("$config.category does not exist in Category enum")
        }

        new GuideMetadata(
                asciidoctor: config.asciidoctor,
                slug: config.slug,
                title: config.title,
                intro: config.intro,
                authors: config.authors,
                tags: config.tags,
                category: cat,
                publicationDate: publish ? LocalDate.parse(config.publicationDate) : null,
                publish: publish,
                base: config.base,
                languages: config.languages ?: ['java', 'groovy', 'kotlin'],
                buildTools: config.buildTools ?: ['gradle', 'maven'],
                testFramework: config.testFramework,
                skipGradleTests: config.skipGradleTests ?: false,
                skipMavenTests: config.skipMavenTests ?: false,
                minimumJavaVersion: config.minimumJavaVersion,
                maximumJavaVersion: config.maximumJavaVersion,
                zipIncludes: config.zipIncludes ?: [],
                apps: config.apps.collect { it -> new App(
                        name: it.name,
                        features: it.features,
                        applicationType: it.applicationType ? ApplicationType.valueOf(it.applicationType.toUpperCase()) : ApplicationType.DEFAULT,
                        excludeSource:  it.excludeSource,
                        excludeTest:  it.excludeTest)
                }
        )
    }

    @CompileDynamic
    void generate(File guidesDir,
                  File output,
                  String metadataConfigName,
                  File projectDir) {

        File asciidocDir = new File(projectDir, 'src/docs/asciidoc')
        if (!asciidocDir.exists()) {
            asciidocDir.mkdir()
        }

        guidesDir.eachDir { dir ->
            GuideMetadata metadata = parseGuideMetadata(dir, metadataConfigName)
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

    private void generateOne(GuideMetadata metadata, File inputDir, File outputDir) {

        if (!outputDir.exists()) {
            assert outputDir.mkdir()
        }

        String packageAndName = BASE_PACKAGE + '.' + APP_NAME
        JdkVersion javaVersion = Utils.parseJdkVersion()

        List<GuidesOption> guidesOptionList = guidesOptions(metadata)
        for (GuidesOption guidesOption : guidesOptionList) {
            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language

            for (App app: metadata.apps) {
                List<String> appFeatures = [] + app.features

                if (guidesOption.language == GROOVY ||
                        !JDK_VERSIONS_SUPPORTED_BY_GRAALVM.contains(javaVersion)) {
                    appFeatures.remove('graalvm')
                }

                if (testFramework == SPOCK) {
                    appFeatures.remove('mockito')
                }

                if (metadata.minimumJavaVersion != null) {
                    JdkVersion minimumJavaVersion = JdkVersion.valueOf(metadata.minimumJavaVersion)
                    if (minimumJavaVersion.majorVersion() > javaVersion.majorVersion()) {
                        javaVersion = minimumJavaVersion
                    }
                }

                // typical guides use 'default' as name, multi-project guides have different modules
                String appName = app.name == DEFAULT_APP_NAME ? EMPTY_STRING : app.name
                String folder = folderName(metadata.slug, guidesOption)

                Path destinationPath = Paths.get(outputDir.absolutePath, folder, appName)
                File destination = destinationPath.toFile()
                destination.mkdir()

                guidesGenerator.generateAppIntoDirectory(destination, app.applicationType, packageAndName,
                        appFeatures, buildTool, testFramework, lang, javaVersion)

                // look for a common 'src' directory shared by multiple languages and copy those files first
                final String srcFolder = 'src'
                Path srcPath = Paths.get(inputDir.absolutePath, appName, srcFolder)
                if (srcPath.toFile().exists()) {
                    Files.walkFileTree(srcPath, new CopyFileVisitor(Paths.get(destination.path, srcFolder)))
                }

                Path sourcePath = Paths.get(inputDir.absolutePath, appName, guidesOption.language.toString())
                if (!sourcePath.toFile().exists()) {
                    throw new GradleException("source folder " + sourcePath.toFile().absolutePath + " does not exist")
                }

                // copy source/resource files for the current language
                Files.walkFileTree(sourcePath, new CopyFileVisitor(destinationPath))

                if (app.excludeSource) {
                    for (String mainSource : app.excludeSource) {
                        deleteFile(destination, GuideAsciidocGenerator.mainPath(appName, mainSource), guidesOption)
                    }
                }

                if (app.excludeTest) {
                    for (String testSource : app.excludeTest) {
                        deleteFile(destination, GuideAsciidocGenerator.testPath(appName,  testSource, testFramework), guidesOption)
                    }
                }

                if (metadata.zipIncludes) {
                    File destinationRoot = new File(outputDir.absolutePath, folder)
                    for (String zipInclude : metadata.zipIncludes) {
                        copyFile(inputDir, destinationRoot, zipInclude)
                    }
                }
            }
        }
    }

    private static void deleteFile(File destination, String path, GuidesOption guidesOption) {
        Paths.get(destination.absolutePath, path
                .replace("@lang@", guidesOption.language.toString())
                .replace("@languageextension@", guidesOption.language.extension))
                .toFile()
                .delete()
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

    static List<GuidesOption> guidesOptions(GuideMetadata guideMetadata) {
        List<String> buildTools = guideMetadata.buildTools
        List<String> languages = guideMetadata.languages
        String testFramework = guideMetadata.testFramework
        List<GuidesOption> guidesOptionList = []

        for (BuildTool buildTool : BuildTool.values()) {
            if (buildTools.contains(buildTool.toString())) {
                for (Language language : Language.values()) {
                    if (languages.contains(language.toString())) {
                        guidesOptionList << createGuidesOption(buildTool, language, testFramework)
                    }
                }
            }
        }

        guidesOptionList
    }

    private static GuidesOption createGuidesOption(@NonNull BuildTool buildTool,
                                                   @NonNull Language language,
                                                   @Nullable String testFramework) {
        new GuidesOption(buildTool, language, testFrameworkOption(language, testFramework))
    }

    private static TestFramework testFrameworkOption(@NonNull Language language,
                                                     @Nullable String testFramework) {
        if (testFramework != null) {
            return TestFramework.valueOf(testFramework.toUpperCase())
        }
        if (language == GROOVY) {
            return SPOCK
        }
        JUNIT
    }
}
