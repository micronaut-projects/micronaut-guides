package io.micronaut.guides

import com.fizzed.rocker.Rocker
import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.util.StringUtils
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.build.dependencies.Coordinate
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver

import java.nio.file.Path
import java.nio.file.Paths
import java.util.Map.Entry

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
            String projectName = "${metadata.slug}-${guidesOption.buildTool.toString()}-${guidesOption.language}"

            List<String> rawLinesExpanded = expandAllCommonIncludes(asciidocFile.readLines(), destinationFolder)

            List<String> lines = []
            boolean excludeLineForLanguage = false
            boolean excludeLineForBuild = false
            boolean groupDependencies = false
            List<String> groupedDependencies = []
            for (String line : rawLinesExpanded) {

                if (line == ':exclude-for-build:') {
                    excludeLineForBuild = false
                } else if (line == ':exclude-for-languages:') {
                    excludeLineForLanguage = false
                }
                if (excludeLineForLanguage || excludeLineForBuild) {
                    continue
                }

                if (shouldProcessLine(line, 'source:')) {
                    lines.addAll(sourceIncludeLines(line))

                } else if (shouldProcessLine(line, 'test:')) {
                    lines.addAll(testIncludeLines(line, guidesOption.testFramework))

                } else if (shouldProcessLine(line, 'rawTest:')) {
                    lines.addAll(rawTestIncludeLines(line, guidesOption.testFramework))

                } else if (shouldProcessLine(line, 'resource:')) {
                    lines.addAll(resourceIncludeLines(line))

                } else if (shouldProcessLine(line, 'testResource:')) {
                    lines.addAll(testResourceIncludeLines(line))

                } else if (shouldProcessLine(line, 'zipInclude:')) {
                    lines.addAll(zipIncludeLines(line))

                } else if (line == ':dependencies:') {
                    groupDependencies = !groupDependencies
                    if (!groupDependencies) {
                        // closing tag. Group and output them all together
                        lines.addAll(DependencyLines.asciidoc(groupedDependencies, guidesOption.buildTool, guidesOption.language))
                        groupedDependencies = []
                    }

                } else if (shouldProcessLine(line, 'dependency:')) {
                    if (groupDependencies) {
                        groupedDependencies.add(line)
                    } else {
                        lines.addAll(DependencyLines.asciidoc(line, guidesOption.buildTool, guidesOption.language))
                    }

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
                } else if (shouldProcessLine(line, 'rocker:')) {
                    lines.addAll(includeRocker(line))
                } else {
                    lines << line
                }
            }
            File versionFile = Paths.get(destinationFolder.absolutePath, "../../../version.txt").toFile()
            String text = lines.join('\n')
            text = text.replace("{githubSlug}", metadata.slug)
            text = text.replace("@language@", StringUtils.capitalize(guidesOption.language.toString()))
            text = text.replace("@guideTitle@", metadata.title)
            text = text.replace("@guideIntro@", metadata.intro)
            text = text.replace("@micronaut@", versionFile.text)
            text = text.replace("@lang@", guidesOption.language.toString())
            text = text.replace("@build@", guidesOption.buildTool.toString())
            text = text.replace("@testFramework@", guidesOption.testFramework.toString())
            text = text.replace("@authors@", metadata.authors.join(', '))
            text = text.replace("@languageextension@", guidesOption.language.extension)
            text = text.replace("@testsuffix@", guidesOption.testFramework == TestFramework.SPOCK ? 'Spec' : 'Test')
            text = text.replace("@sourceDir@", projectName)
            text = text.replace("@api@", 'https://docs.micronaut.io/latest/api')

            for (Entry<String, Coordinate> entry : getCoordinates().entrySet()) {
                text = text.replace("@${entry.key}Version@", entry.value.version)
            }

            Path destinationPath = Paths.get(destinationFolder.absolutePath, projectName + ".adoc")
            File destination = destinationPath.toFile()
            destination.createNewFile()
            destination.text = text
        }
    }

    private static List<String> expandAllCommonIncludes(List<String> lines, File destinationFolder) {
        List<String> rawLines = []

        for (String rawLine : lines) {
            if (rawLine.startsWith('include::{commondir}/') && rawLine.endsWith('[]')) {
                String commonFileName = rawLine.substring(rawLine.indexOf('include::{commondir}/') + 'include::{commondir}/'.length(), rawLine.indexOf('[]'))

                File commonFile = Paths.get(destinationFolder.absolutePath, "../common/$commonFileName").toFile()
                assert commonFile.exists()

                rawLines.add("// Start: ${commonFileName}".toString())
                rawLines.addAll(expandAllCommonIncludes(commonFile.readLines(), destinationFolder))
                rawLines.add("// End: ${commonFileName}".toString())
            } else {
                rawLines << rawLine
            }
        }

        return rawLines
    }

    private static boolean shouldProcessLine(String line, String macro) {
        line.startsWith(macro) && line.contains('[') && line.endsWith(']')
    }

    private static String extractName(String line, String macro) {
        line.substring(macro.length(), line.indexOf('['))
    }

    private static List<String> sourceIncludeLines(String line) {
        sourceIncludeLines(line, null, 'source:')
    }

    private static List<String> testIncludeLines(String line, TestFramework testFramework) {
        sourceIncludeLines(line, testFramework, 'test:')
    }

    private static List<String> resourceIncludeLines(String line) {
        resourceIncludeLines(line, 'main', 'resource:')
    }

    private static List<String> testResourceIncludeLines(String line) {
        resourceIncludeLines(line, 'test', 'testResource:')
    }

    private static List<String> zipIncludeLines(String line) {
        String fileName = extractName(line, 'zipInclude:')
        List<String> tagNames = extractTags(line)

        List<String> tags = tagNames ? tagNames.collect { "tag=" + it } : []
        String asciidoctorLang = resolveAsciidoctorLanguage(fileName)

        List<String> lines = [
                '[source,' + asciidoctorLang + ']',
                '.' + fileName,
                '----']
        if (tags) {
            for (String tag : tags) {
                lines << "include::{sourceDir}/@sourceDir@/${fileName}[${tag}]\n".toString()
            }
        } else {
            lines << "include::{sourceDir}/@sourceDir@/${fileName}[]".toString()
        }
        lines << '----'
        lines
    }

    private static List<String> sourceIncludeLines(String line, TestFramework testFramework, String macro) {
        String name = extractName(line, macro)
        String appName = extractAppName(line)
        List<String> tagNames = extractTags(line)

        List<String> tags = tagNames ? tagNames.collect { "tag=" + it } : []

        String sourcePath = testFramework ? testPath(appName, name, testFramework) : mainPath(appName, name)
        List<String> lines = [
            '[source,@lang@]',
            ".${sourcePath}".toString(),
            '----',
        ]
        if (tags) {
            for (String tag : tags) {
                lines.add("include::{sourceDir}/@sourceDir@/${sourcePath}[${tag}]\n".toString())
            }
        } else {
            lines.add("include::{sourceDir}/@sourceDir@/${sourcePath}[]".toString())
        }

        lines.add('----')
        lines
    }

    @NonNull
    static String mainPath(@NonNull String appName,
                           @NonNull String fileName) {
        pathByFolder(appName, fileName, 'main')
    }

    @NonNull
    static String testPath(@NonNull String appName,
                           @NonNull String name,
                           @NonNull TestFramework testFramework) {
        String fileName = name
        if (testFramework) {
            if (name.endsWith('Test')) {
                fileName = name.substring(0, name.indexOf('Test'))
                fileName += testFramework == TestFramework.SPOCK ? 'Spec' : 'Test'
            }
        }

        pathByFolder(appName, fileName, 'test')
    }

    @NonNull
    private static String pathByFolder(@NonNull String appName,
                                       @NonNull String fileName,
                                       String folder) {

        String module = appName ? "${appName}/" : ""
        "${module}src/${folder}/@lang@/example/micronaut/${fileName}.@languageextension@".toString()
    }

    private static List<String> rawTestIncludeLines(String line, TestFramework testFramework) {
        String fileName = extractName(line, 'rawTest:')
        String appName = extractAppName(line)
        List<String> tagNames = extractTags(line)

        String module = appName ? "${appName}/" : ""
        List<String> tags = tagNames ? tagNames.collect { "tag=" + it } : []

        String fileExtension = testFramework.toTestFramework().defaultLanguage.getExtension()
        String langTestFolder = testFramework.toTestFramework().defaultLanguage.getTestSrcDir()

        List<String> lines = [
            "[source,${fileExtension}]".toString(),
            ".${module}${langTestFolder}/example/micronaut/${fileName}.${fileExtension}".toString(),
            '----',
        ]
        if (tags) {
            for (String tag : tags) {
                lines.add("include::{sourceDir}/@sourceDir@/${module}${langTestFolder}/example/micronaut/${fileName}.${fileExtension}[${tag}]\n".toString())
            }
        } else {
            lines.add("include::{sourceDir}/@sourceDir@/${module}${langTestFolder}/example/micronaut/${fileName}.${fileExtension}[]".toString())
        }

        lines.add('----')
        lines
    }

    private static List<String> resourceIncludeLines(String line, String resourceDir, String macro) {
        String fileName = extractName(line, macro)
        String appName = extractAppName(line)
        List<String> tagNames = extractTags(line)

        String module = appName ? "${appName}/" : ""
        List<String> tags = tagNames ? tagNames.collect { "tag=" + it } : []
        String asciidoctorLang = resolveAsciidoctorLanguage(fileName)

        List<String> lines = [
            "[source,${asciidoctorLang}]".toString(),
            ".${module}src/${resourceDir}/resources/${fileName}".toString(),
            "----",
        ]
        if (tags) {
            for (String tag : tags) {
                lines.add("include::{sourceDir}/@sourceDir@/${module}src/${resourceDir}/resources/${fileName}[${tag}]\n".toString())
            }
        } else {
            lines.add("include::{sourceDir}/@sourceDir@/${module}src/${resourceDir}/resources/${fileName}[]".toString())
        }
        lines << '----'
        lines
    }

    private static List<String> includeRocker(String line) {
        String name = extractName(line, 'rocker:')
        String rendered = Rocker.template("io/micronaut/guides/feature/template/${name}.rocker.raw").render()
        return rendered.split("\\r?\\n|\\r") as List
    }

    private static String extractAppName(String line) {
        extractFromParametersLine(line, 'app')
    }

    private static String extractTagName(String line) {
        extractFromParametersLine(line, 'tag')
    }

    private static List<String> extractTags(String line, String tagSeparator = '|') {
        String attributeValue = extractFromParametersLine(line, 'tags')
        if (attributeValue) {
            return attributeValue.tokenize(tagSeparator)
        }

        [extractTagName(line)].findAll { it }
    }

    private static String extractFromParametersLine(String line, String attributeName) {

        List<String> attrs = line.substring(line.indexOf("[") + 1, line.indexOf("]")).tokenize(",")

        return attrs
            .stream()
            .filter({ it.startsWith(attributeName) })
            .map({ it.tokenize("=") })
            .map({ it.get(1) })
            .findFirst()
            .orElse("")
    }

    private static String resolveAsciidoctorLanguage(String fileName) {
        String extension = fileName.indexOf(".") > 0 ?
            fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : ''

        switch (extension.toLowerCase()) {
            case 'yml':
            case 'yaml':
                return 'yaml'
            case 'html':
            case 'vm':
            case 'hbs':
                return 'html'
            case 'xml':
                return 'xml'
            default:
                return extension.toLowerCase()
        }
    }

    private static Map<String, Coordinate> getCoordinates() {
        try (ApplicationContext context = ApplicationContext.run()) {
            PomDependencyVersionResolver pomDependencyVersionResolver = context.getBean(PomDependencyVersionResolver)
            return pomDependencyVersionResolver.getCoordinates()
        }
    }
}
