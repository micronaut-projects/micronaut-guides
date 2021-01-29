package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.core.util.StringUtils
import io.micronaut.starter.api.TestFramework

import java.nio.file.Path
import java.nio.file.Paths

@CompileStatic
class GuideAsciidocGenerator {

    static void generate(GuideMetadata metadata, File inputDir, File destinationFolder) {
        Path asciidocPath = Paths.get(inputDir.absolutePath, metadata.asciidoctor)
        File asciidocFile = asciidocPath.toFile()
        assert asciidocFile.exists()
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir()
        }
        List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
        for (GuidesOption guidesOption : guidesOptionList) {
            String projectName = "${metadata.slug}-${guidesOption.buildTool.toString()}-${guidesOption.language.toString()}"
            List<String> rawLines = []
            String rawLine = ''
            asciidocFile.withReader { reader ->

                while ((rawLine = reader.readLine()) != null) {
                    if (rawLine.startsWith('include::{commondir}/') && rawLine.endsWith('[]')) {
                        String commonFileName = rawLine.substring(rawLine.indexOf('include::{commondir}/') + 'include::{commondir}/'.length(), rawLine.indexOf('[]'))

                        File commonFile = Paths.get(destinationFolder.absolutePath, "../common/$commonFileName").toFile()
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
                if (shouldProcessLine(line, 'source:')) {
                    lines.addAll(sourceIncludeLines(extractName(line, 'source:'), extractAppName(line)))

                } else if (shouldProcessLine(line, 'test:')) {
                    lines.addAll(sourceIncludeLines(extractName(line, 'test:'), extractAppName(line), guidesOption.testFramework))

                } else if (shouldProcessLine(line, 'resource:')) {
                    lines.addAll(resourceIncludeLines(extractName(line, 'resource:'), extractAppName(line), extractTagName(line)))

                } else if (shouldProcessLine(line, 'testResource:')) {
                    lines.addAll(testResourceIncludeLines(extractName(line, 'testResource:'), extractAppName(line), extractTagName(line)))

                } else if (shouldProcessLine(line, 'dependency:')) {
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
            text = text.replace("{githubSlug}", metadata.slug)
            text = text.replace("@language@", StringUtils.capitalize(guidesOption.language.toString()))
            text = text.replace("@guideTitle@", metadata.title)
            text = text.replace("@guideIntro@", metadata.intro)
            File versionFile = Paths.get(destinationFolder.absolutePath, "../../../version.txt").toFile()
            text = text.replace("@micronaut@", versionFile.text)
            text = text.replace("@lang@", guidesOption.language.toString())
            text = text.replace("@build@", guidesOption.buildTool.toString())
            text = text.replace("@testFramework@", guidesOption.testFramework.toString())
            text = text.replace("@authors@", metadata.authors.join(', '))
            text = text.replace("@languageextension@", guidesOption.language.extension)
            text = text.replace("@testsuffix@", guidesOption.testFramework == TestFramework.SPOCK ? 'Spec' : 'Test')

            text = text.replace("@sourceDir@", projectName)

            Path destinationPath = Paths.get(destinationFolder.absolutePath, projectName + ".adoc")
            File destination = destinationPath.toFile()
            destination.createNewFile()
            destination.text = text
        }
    }

    static boolean shouldProcessLine(String line, String macro) {
        line.startsWith(macro) && line.contains('[') && line.endsWith(']')
    }

    static String extractName(String line, String macro) {
        line.substring(macro.length(), line.indexOf('['))
    }

    static List<String> sourceIncludeLines(String name,
                                           String appName,
                                           TestFramework testFramework = null) {
        String fileName = name
        if (testFramework) {
            if (name.endsWith('Test')) {
                fileName = name.substring(0, name.indexOf('Test'))
                fileName += testFramework == TestFramework.SPOCK ? 'Spec' : 'Test'
            }
        }
        String folder = testFramework ? 'test' : 'main'
        String module = appName ? "${appName}/" : ""

        [
            '[source,@lang@]',
            ".${module}src/${folder}/@lang@/example/micronaut/${fileName}.@languageextension@",
            '----',
            "include::{sourceDir}/@sourceDir@/${module}src/${folder}/@lang@/example/micronaut/${fileName}.@languageextension@[]",
            '----',
        ] as List<String>
    }

    static List<String> resourceIncludeLines(String name,
                                             String appName,
                                             String tagName) {
        String fileName = name
        String module = appName ? "${appName}/" : ""
        String tag = tagName ? "tag=${tagName}" : ""

        [
            '[source,@lang@]',
            ".${module}src/main/resources/${fileName}",
            '----',
            "include::{sourceDir}/@sourceDir@/${module}src/main/resources/${fileName}[${tag}]",
            '----',
        ] as List<String>
    }

    static List<String> testResourceIncludeLines(String name,
                                                 String appName,
                                                 String tagName) {
        String fileName = name
        String module = appName ? "${appName}/" : ""
        String tag = tagName ? "tag=${tagName}" : ""

        [
            '[source,@lang@]',
            ".${module}src/test/resources/${fileName}",
            '----',
            "include::{sourceDir}/@sourceDir@/${module}src/test/resources/${fileName}[${tag}]",
            '----',
        ] as List<String>
    }

    static String extractAppName(String line) {
        extractFromParametersLine(line, 'app')
    }

    static String extractTagName(String line) {
        extractFromParametersLine(line, 'tag')
    }

    static String extractFromParametersLine(String line, String attributeName) {
        List<String> attrs = line.substring(line.indexOf("[") + 1, line.indexOf("]")).tokenize(",")

        return attrs
            .stream()
            .filter({ it.startsWith(attributeName)})
            .map( {it.tokenize("=")})
            .map({it.get(1)})
            .findFirst()
            .orElse("")
    }
}
