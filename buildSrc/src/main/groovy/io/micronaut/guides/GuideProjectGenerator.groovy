package io.micronaut.guides

import edu.umd.cs.findbugs.annotations.NonNull
import edu.umd.cs.findbugs.annotations.Nullable
import io.micronaut.context.ApplicationContext
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import org.gradle.api.GradleException

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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

    void generate(GuideMetadata metadata, File inputDir, File outputDir) {
        String packageAndName = "${basePackage}.${appName}"
        ApplicationType type = ApplicationType.DEFAULT
        String name = metadata.slug
        List<GuidesOption> guidesOptionList = guidesOptions(metadata)
        for (GuidesOption guidesOption : guidesOptionList) {
            List<String> guidesFeatures = metadata.features
            if (guidesOption.language == Language.GROOVY) {
                guidesFeatures.remove('graalvm')
            }
            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language
            JdkVersion javaVersion = JdkVersion.JDK_8
            String folder = "${name}-${guidesOption.buildTool.toString()}-${guidesOption.language.toString()}"
            if (!outputDir.exists()) {
                outputDir.mkdir()
            }
            Path destinationPath = Paths.get(outputDir.absolutePath, folder)
            File destination = destinationPath.toFile()
            destination.mkdir()
            guidesGenerator.generateAppIntoDirectory(destination, type, packageAndName, guidesFeatures, buildTool, testFramework, lang, javaVersion)
            Path sourcePath = Paths.get(inputDir.absolutePath, guidesOption.language.toString())
            if (!sourcePath.toFile().exists()) {
                throw new GradleException("source folder " + source.absolutePath + " does not exist")
            }
            Files.walkFileTree(sourcePath, new CopyFileVisitor(destinationPath))
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
