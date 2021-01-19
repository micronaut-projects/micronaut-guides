package io.micronaut.guides

import io.micronaut.core.util.StringUtils
import io.micronaut.starter.api.TestFramework

import java.nio.file.Path
import java.nio.file.Paths

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
                if (line.startsWith('source:') && line.contains('[') && line.endsWith(']')) {
                    lines.addAll(sourceIncludeLines(line.substring('source:'.length(), line.indexOf('['))))
                } else if (line.startsWith('test:') && line.contains('[') && line.endsWith(']')) {
                    lines.addAll(sourceIncludeLines(line.substring('test:'.length(), line.indexOf('[')), guidesOption.testFramework))
                } else if (line.startsWith('dependency:') && line.contains('[') && line.endsWith(']')) {
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
            text = text.replace("@micronaut@", new File("version.txt").text)
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

    static List<String> sourceIncludeLines(String name,
                                           TestFramework testFramework = null) {
        String fileName = name
        if (testFramework) {
            if (name.endsWith('Test')) {
                fileName = name.substring(0, name.indexOf('Test'))
                fileName += testFramework == TestFramework.SPOCK ? 'Spec' : 'Test'
            }
        }
        String folder = testFramework ? 'test' : 'main'
[
'[source,@lang@]',
".src/${folder}/@lang@/example/micronaut/${fileName}.@languageextension@",
'----',
"include::{sourceDir}/@sourceDir@/src/${folder}/@lang@/example/micronaut/${fileName}.@languageextension@[]",
'----',
]
    }
}
