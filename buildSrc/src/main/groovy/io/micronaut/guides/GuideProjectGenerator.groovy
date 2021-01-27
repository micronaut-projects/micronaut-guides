package io.micronaut.guides

import edu.umd.cs.findbugs.annotations.NonNull
import edu.umd.cs.findbugs.annotations.Nullable
import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
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

@CompileStatic
class GuideProjectGenerator implements Closeable {

    private final ApplicationContext applicationContext
    private final GuidesGenerator guidesGenerator

    String basePackage = 'example.micronaut'
    String appName = 'complete'

    GuideProjectGenerator() {
        this.applicationContext = ApplicationContext.run()
        guidesGenerator = applicationContext.getBean(GuidesGenerator)
    }

    @Override
    void close() throws IOException {
        applicationContext.close()
    }

    @CompileDynamic
    void generate(File guidesFolder,
                  File output,
                  String metadataConfigName,
                  boolean merge = true,
                  File asciidocDir = null) {
        guidesFolder.eachDir { dir ->
            File configFile = new File("$dir/$metadataConfigName")
            if (!configFile.exists()) {
                throw new GradleException("metadata file not found for ${dir.name}")
            }
            def config = new JsonSlurper().parse(configFile)
            GuideMetadata metadata = new GuideMetadata(asciidoctor: config.asciidoctor,
                    slug: config.slug,
                    title: config.title,
                    intro: config.intro,
                    authors: config.authors,
                    languages: config.languages ?: ['java', 'groovy', 'kotlin'],
                    buildTools: config.buildTools ?: ['gradle', 'maven'],
                    testFramework: config.testFramework,
                    skipGradleTests: config.skipGradleTests ?: false,
                    skipMavenTests: config.skipMavenTests ?: false,
                    apps: config.apps.collect { it -> new App(name: it.name, features: it.features) }
            )
            generate(metadata, dir, output, merge)
            if (asciidocDir != null) {
                GuideAsciidocGenerator.generate(metadata, dir, asciidocDir)
            }
        }
    }

    void generate(GuideMetadata metadata, File inputDir, File outputDir, boolean merge = true) {
        String packageAndName = "${basePackage}.${appName}"
        ApplicationType type = ApplicationType.DEFAULT
        String name = metadata.slug

        List<GuidesOption> guidesOptionList = guidesOptions(metadata)
        for (GuidesOption guidesOption : guidesOptionList) {

            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language
            JdkVersion javaVersion = JdkVersion.JDK_8

            if (!outputDir.exists()) {
                outputDir.mkdir()
            }

            for (App app: metadata.apps) {
                List<String> appFeatures = app.features
                if (guidesOption.language == Language.GROOVY) {
                    appFeatures.remove('graalvm')
                }

                // Normal guide use 'default' as name, multi project guides have different modules
                String appName = app.name == 'default' ? "" : app.name

                String folder = "${name}-${guidesOption.buildTool.toString()}-${guidesOption.language.toString()}"

                Path destinationPath = Paths.get(outputDir.absolutePath, folder, appName)
                File destination = destinationPath.toFile()
                destination.mkdir()
                guidesGenerator.generateAppIntoDirectory(destination, type, packageAndName, appFeatures, buildTool, testFramework, lang, javaVersion)
                if (merge) {
                    Path sourcePath = Paths.get(inputDir.absolutePath, appName, guidesOption.language.toString())
                    if (!sourcePath.toFile().exists()) {
                        throw new GradleException("source folder " + sourcePath.toFile().absolutePath + " does not exist")
                    }
                    Files.walkFileTree(sourcePath, new CopyFileVisitor(destinationPath))
                }
            }
        }
    }

    static List<GuidesOption> guidesOptions(GuideMetadata guideMetadata) {
        List<String> buildTools = guideMetadata.buildTools
        List<String> languages = guideMetadata.languages
        String testFramework = guideMetadata.testFramework
        List<GuidesOption> guidesOptionList = []

        if (buildTools.contains(BuildTool.GRADLE.toString())) {
            if (languages.contains(Language.JAVA.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.GRADLE, Language.JAVA, testFramework)
            }
            if (languages.contains(Language.KOTLIN.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.GRADLE, Language.KOTLIN, testFramework)
            }
            if (languages.contains(Language.GROOVY.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.GRADLE, Language.GROOVY, testFramework)
            }
        }
        if (buildTools.contains(BuildTool.MAVEN.toString())) {
            if (languages.contains(Language.JAVA.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.MAVEN, Language.JAVA, testFramework)
            }
            if (languages.contains(Language.KOTLIN.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.MAVEN, Language.KOTLIN, testFramework)
            }
            if (languages.contains(Language.GROOVY.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.MAVEN, Language.GROOVY, testFramework)
            }
        }
        guidesOptionList
    }

    static GuidesOption createGuidesOption(@NonNull BuildTool buildTool,
                                           @NonNull Language language,
                                           @Nullable String testFramework) {
        new GuidesOption(buildTool, language, testFrameworkOption(language, testFramework))
    }

    static TestFramework testFrameworkOption(@NonNull Language language,
                                             @Nullable String testFramework) {
        if (testFramework != null) {
            return TestFramework.valueOf(testFramework.toUpperCase())
        }
        if (language == Language.GROOVY) {
            return TestFramework.SPOCK
        }
        TestFramework.JUNIT
    }
}
