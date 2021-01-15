package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.core.util.StringUtils
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class GenerateGuidesTask  extends DefaultTask {
    @OutputDirectory
    File output

    @Input
    String guideName

    @Input
    String micronaut

    @Input
    String guideTitle

    @Input
    String guideIntro

    @Input
    List<String> authors

    @Input
    String asciidocFileName

    @Input
    List<String> buildTools = ['gradle', 'maven']

    @Input
    List<String> languages = ['java', 'groovy', 'kotlin']

    @Optional
    @Input
    String testFramework

    @Input
    List<String> features

    @Input
    String basePackage = 'example.micronaut';

    @Input
    String appName = 'complete'

    private List<GuidesOption> guidesOptions() {
        List<GuidesOption> guidesOptionList = []
        if (buildTools.contains(BuildTool.GRADLE.toString())) {
            if (languages.contains(Language.JAVA.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.GRADLE, Language.JAVA)
            }
            if (languages.contains(Language.KOTLIN.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.GRADLE, Language.KOTLIN)
            }
            if (languages.contains(Language.GROOVY.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.GRADLE, Language.GROOVY)
            }
        }
        if (buildTools.contains(BuildTool.MAVEN.toString())) {
            if (languages.contains(Language.JAVA.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.MAVEN, Language.JAVA)
            }
            if (languages.contains(Language.KOTLIN.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.MAVEN, Language.KOTLIN)
            }
            if (languages.contains(Language.GROOVY.toString())) {
                guidesOptionList << createGuidesOption(BuildTool.MAVEN, Language.GROOVY)
            }
        }
        guidesOptionList
    }

    GuidesOption createGuidesOption(BuildTool buildTool, Language language) {
        new GuidesOption(buildTool, language, testFrameworkOption(language))
    }

    TestFramework testFrameworkOption(Language language) {
        if (testFramework != null) {
            return TestFramework.valueOf(testFramework.toUpperCase())
        }
        if(language == Language.GROOVY) {
            return TestFramework.SPOCK
        }
        TestFramework.JUNIT
    }

    @TaskAction
    void renderGuide() {
        ApplicationContext applicationContext = ApplicationContext.run()

        GuidesGenerator guidesGenerator = applicationContext.getBean(GuidesGenerator)

        ApplicationType type = ApplicationType.DEFAULT
        String name = guideName
        String pakckageAndName = "${basePackage}.${appName}"

        List<GuidesOption> guidesOptionList = guidesOptions()
        for (GuidesOption guidesOption : guidesOptionList) {
            List<String> guidesFeatures = features
            if (guidesOption.language == Language.GROOVY) {
                guidesFeatures.remove('graalvm')
            }
            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language
            JdkVersion javaVersion = JdkVersion.JDK_8
            String folder = "${name}-${guidesOption.buildTool.toString()}-${guidesOption.language.toString()}"
            if (!output.exists()) {
                output.mkdir()
            }
            File destination = new File("${output.absolutePath}/${folder}")
            destination.mkdir()
            guidesGenerator.generateAppIntoDirectory(destination, type, pakckageAndName, features, buildTool, testFramework, lang, javaVersion)
        }

        File asciidocFile = new File("${project.rootDir}/src/docs/guides/${asciidocFileName}")
        assert asciidocFile.exists()
        File destinationFolder = new File('src/docs/asciidoc')
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir()
        }
        for (GuidesOption guidesOption : guidesOptionList) {
            String projectName = "${asciidocFileName.replace('.adoc', '')}-${guidesOption.buildTool.toString()}-${guidesOption.language.toString()}"
            List<String> rawLines = []
            String rawLine = ''
            asciidocFile.withReader { reader ->

                while ((rawLine = reader.readLine()) != null) {
                    if (rawLine.startsWith('include::{commondir}/') && rawLine.endsWith('[]')) {
                        String commonFileName = rawLine.substring(rawLine.indexOf('include::{commondir}/') + 'include::{commondir}/'.length(), rawLine.indexOf('[]'))
                        File commonFile = new File("src/docs/common/$commonFileName")
                        assert commonFile.exists()
                        commonFile.withReader { commonReader ->
                            while ((rawLine = commonReader.readLine()) != null) {
                                rawLines << rawLine
                            }
                        }
                    } else {
                        rawLines << rawLine
                    }

                }
            }
            List<String> lines = []
            boolean excludeLineForLanguage = false
            boolean excludeLineForBuild = false
            for (String line : rawLines) {
                if (line.startsWith('dependency:') && line.contains('[') && line.endsWith(']')) {
                    lines.addAll(DependencyLines.asciidoc(line, guidesOption.buildTool))

                } else if (line == ':exclude-for-build:') {
                    excludeLineForBuild = false

                } else if (line == ':exclude-for-languages:') {
                    excludeLineForLanguage = false

                } else if (line.startsWith(':exclude-for-build:')) {
                    String[] builds = line.substring(':exclude-for-build:'.length()).split(',')
                    if (builds.any { it == guidesOption.buildTool.toString() }) {
                        excludeLineForBuild = true
                    }

                } else if (line.startsWith(':exclude-for-languages:')) {
                    String[] languages = line.substring(':exclude-for-languages:'.length()).split(',')
                    if (languages.any { it == guidesOption.language.toString() }) {
                        excludeLineForLanguage = true
                    }
                } else {
                    if (!excludeLineForLanguage && !excludeLineForBuild) {
                        lines << line
                    }
                }
            }
            String text = lines.join('\n')
            text = text.replace("{githubSlug}", asciidocFileName.replace('.adoc', ''))
            text = text.replace("@language@", StringUtils.capitalize(guidesOption.language.toString()))
            text = text.replace("@guideTitle@", guideTitle)
            text = text.replace("@guideIntro@", guideIntro)
            text = text.replace("@micronaut@", micronaut)
            text = text.replace("@lang@", guidesOption.language.toString())
            text = text.replace("@build@", guidesOption.buildTool.toString())
            text = text.replace("@authors@", authors.join(', '))
            text = text.replace("@languageextension@", guidesOption.language.extension)
            text = text.replace("@testsuffix@", guidesOption.testFramework == TestFramework.SPOCK ? 'Spec' : 'Test')
            text = text.replace("@language@", guidesOption.language.toString())
            text = text.replace("@sourceDir@", projectName)
            File destination = new File("src/docs/asciidoc/${projectName}.adoc")
            destination.createNewFile()
            destination.text = text
        }
        applicationContext.close()
    }
}
