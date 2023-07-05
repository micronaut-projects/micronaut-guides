package io.micronaut.guides

import com.fizzed.rocker.Rocker
import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.util.StringUtils
import io.micronaut.guides.GuideMetadata.App
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.build.dependencies.Coordinate
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.util.VersionInfo
import org.gradle.api.GradleException

import java.nio.file.Paths
import java.util.Map.Entry
import java.util.regex.Matcher
import java.util.regex.Pattern

import static io.micronaut.starter.api.TestFramework.SPOCK
import static io.micronaut.starter.application.ApplicationType.CLI
import static io.micronaut.starter.application.ApplicationType.DEFAULT
import static io.micronaut.starter.application.ApplicationType.FUNCTION
import static io.micronaut.starter.application.ApplicationType.GRPC
import static io.micronaut.starter.application.ApplicationType.MESSAGING
import static io.micronaut.starter.options.Language.GROOVY

@CompileStatic
class GuideAsciidocGenerator {

    private static final String INCLUDE_COMMONDIR = 'common:'
    private static final String CALLOUT = 'callout:'
    private static final String EXTERNAL = 'external:'
    private static final String EXTERNAL_PARAMETRIZED = 'external-template:'
    private static final String COMMON_PARAMETRIZED = 'common-template:'
    private static final Pattern GUIDE_LINK_REGEX = ~/(.*)guideLink:(.*)\[(.*)](.*)/
    private static final String CLI_MESSAGING = 'create-messaging-app'
    private static final String CLI_DEFAULT = 'create-app'
    private static final String CLI_GRPC = 'create-grpc-app'
    private static final String CLI_FUNCTION = 'create-function-app'
    private static final String CLI_CLI = 'create-cli-app'

    public static final int DEFAULT_MIN_JDK = 17
    public static final String EXCLUDE_FOR_LANGUAGES = ':exclude-for-languages:'
    public static final String EXCLUDE_FOR_JDK_LOWER_THAN = ':exclude-for-jdk-lower-than:'
    public static final String EXCLUDE_FOR_BUILD = ':exclude-for-build:'
    public static final String DEFAULT_APP_NAME = "default"

    static void generate(GuideMetadata metadata, File inputDir,
                         File asciidocDir, File projectDir) {

        JdkVersion javaVersion = Utils.parseJdkVersion()
        if (metadata.maximumJavaVersion != null && javaVersion.majorVersion() > metadata.maximumJavaVersion) {
            println "not generating asciidoc for $metadata.slug, JDK ${javaVersion.majorVersion()} > $metadata.maximumJavaVersion"
            return
        }

        File asciidocFile = new File(inputDir, metadata.asciidoctor)
        assert asciidocFile.exists()

        List<String> rawLinesExpanded = expandMacros(asciidocFile.readLines(), projectDir)

        List<GuidesOption> guidesOptionList = GuideProjectGenerator.guidesOptions(metadata)
        for (GuidesOption guidesOption : guidesOptionList) {
            String projectName = "${metadata.slug}-${guidesOption.buildTool}-${guidesOption.language}"

            List<String> lines = []
            boolean excludeLineForLanguage = false
            boolean excludeLineForMinJdk = false
            boolean excludeLineForBuild = false
            boolean groupDependencies = false
            List<String> groupedDependencies = []
            for (String line : rawLinesExpanded) {

                if (line == EXCLUDE_FOR_BUILD) {
                    excludeLineForBuild = false
                } else if (line == EXCLUDE_FOR_LANGUAGES) {
                    excludeLineForLanguage = false
                } else if (line == EXCLUDE_FOR_JDK_LOWER_THAN) {
                    excludeLineForMinJdk = false
                }
                if (excludeLineForLanguage || excludeLineForBuild || excludeLineForMinJdk) {
                    continue
                }

                if (shouldProcessLine(line, 'source:')) {
                    lines.addAll(sourceIncludeLines(metadata.slug, line))

                } else if (shouldProcessLine(line, 'test:')) {
                    lines.addAll(testIncludeLines(metadata.slug, line, guidesOption.testFramework))

                } else if (shouldProcessLine(line, 'rawTest:')) {
                    lines.addAll(rawTestIncludeLines(metadata.slug, line, guidesOption.testFramework))

                } else if (shouldProcessLine(line, 'resource:')) {
                    lines.addAll(resourceIncludeLines(metadata.slug, line))

                } else if (shouldProcessLine(line, 'testResource:')) {
                    lines.addAll(testResourceIncludeLines(metadata.slug, line))

                } else if (shouldProcessLine(line, 'zipInclude:')) {
                    lines.addAll(zipIncludeLines(metadata.slug, line))

                } else if (line == ':dependencies:') {
                    groupDependencies = !groupDependencies
                    if (!groupDependencies) {
                        // closing tag. Group and output them all together
                        lines.addAll(DependencyLines.asciidoc(groupedDependencies, guidesOption.buildTool, guidesOption.language))
                        groupedDependencies = []
                    }

                } else if (shouldProcessLine(line, 'dependency:')) {
                    if (groupDependencies) {
                        groupedDependencies << line
                    } else {
                        lines.addAll(DependencyLines.asciidoc(line, guidesOption.buildTool, guidesOption.language))
                    }

                } else if (line.startsWith(EXCLUDE_FOR_BUILD)) {
                    String[] builds = line.substring(EXCLUDE_FOR_BUILD.length()).split(',')
                    if (builds.any { it == guidesOption.buildTool.toString() }) {
                        excludeLineForBuild = true
                    }
                } else if (line.startsWith(EXCLUDE_FOR_LANGUAGES)) {
                    String[] languages = line.substring(EXCLUDE_FOR_LANGUAGES.length()).split(',')
                    if (languages.any { it == guidesOption.language.toString() }) {
                        excludeLineForLanguage = true
                    }
                } else if (line.startsWith(EXCLUDE_FOR_JDK_LOWER_THAN)) {
                    try {
                        String str = line.substring(EXCLUDE_FOR_JDK_LOWER_THAN.length())
                        if (StringUtils.isNotEmpty(str)) {
                            Integer minJdk = Integer.valueOf(str)
                            if ((metadata.minimumJavaVersion ?: DEFAULT_MIN_JDK) >= minJdk) {
                                excludeLineForMinJdk = true
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
                } else if (shouldProcessLine(line, 'rocker:')) {
                    lines.addAll(includeRocker(line))
                } else if (shouldProcessLine(line, 'diffLink:')) {
                    lines << buildDiffLink(line, guidesOption, metadata)
                } else {
                    lines << line
                }
            }

            String version = new File(projectDir, 'version.txt').text.trim()

            String text = lines.join('\n')
            text = text.replace("{githubSlug}", metadata.slug)
            text = text.replace("@language@", StringUtils.capitalize(guidesOption.language.toString()))
            text = text.replace("@guideTitle@", metadata.title)
            text = text.replace("@guideIntro@", metadata.intro)
            text = text.replace("@micronaut@", version)
            text = text.replace("@lang@", guidesOption.language.toString())
            text = text.replace("@build@", guidesOption.buildTool.toString())
            text = text.replace("@testFramework@", guidesOption.testFramework.toString())
            text = text.replace("@authors@", metadata.authors.join(', '))
            text = text.replace("@languageextension@", guidesOption.language.extension)
            text = text.replace("@testsuffix@", guidesOption.testFramework == SPOCK ? 'Spec' : 'Test')
            text = text.replace("@sourceDir@", projectName)
            text = text.replace("@minJdk@", metadata.minimumJavaVersion?.toString() ?: "17")
            text = text.replace("@api@", 'https://docs.micronaut.io/latest/api')

            text = text.replaceAll(~/@([\w-]*):?cli-command@/) { List<String> matches ->
                String app = matches[1] ?: 'default'
                cliCommandForApp(metadata, app)
                        .orElseThrow {
                            new GradleException("No CLI command found for app: $app -- should be one of ${metadata.apps.name.collect { "@$it:cli-command@" }.join(", ")}")
                        }
            }

            text = text.replaceAll(~/@([\w-]*):?features@/) { List<String> matches ->
                String app = matches[1] ?: 'default'
                featuresForApp(metadata, guidesOption, app).join(',')
            }

            text = text.replaceAll(~/@([\w-]*):?features-words@/) { List<String> matches ->
                String app = matches[1] ?: 'default'
                featuresWordsForApp(metadata, guidesOption, app)
            }

            for (Entry<String, Coordinate> entry : getCoordinates().entrySet()) {
                if (entry.value.version) {
                    text = text.replace("@${entry.key}Version@", entry.value.version)
                }
            }
            text = text.replace("@micronautVersion@", VersionInfo.getMicronautVersion())

            File renderedAsciidocFile = new File(asciidocDir, projectName + '.adoc')
            renderedAsciidocFile.createNewFile()
            renderedAsciidocFile.setText(text, 'UTF-8')
        }
    }

    private static Optional<String> cliCommandForApp(GuideMetadata metadata,
                                                     String appName) {
        App app = metadata.apps.find { it.name == appName }
        if (app) {
            return Optional.of(cliCommandForApp(app))
        }
        Optional.empty()
    }

    private static String cliCommandForApp(App app) {
        switch (app.getApplicationType()) {
            case CLI:
                return CLI_CLI
            case FUNCTION:
                return CLI_FUNCTION
            case GRPC:
                return CLI_GRPC
            case MESSAGING:
                return CLI_MESSAGING
            case DEFAULT:
                return CLI_DEFAULT
        }
    }

    private static String featuresWordsForApp(GuideMetadata metadata,
                                              GuidesOption guidesOption,
                                              String app) {
        List<String> features = featuresForApp(metadata, guidesOption, app)
                .collect { '`' + it + '`' }
        if (features.size() > 1) {
            return "${features[0..-2].join(', ')}, and ${features[-1]}"
        }
        features[0]
    }

    private static List<String> featuresForApp(GuideMetadata metadata,
                                               GuidesOption guidesOption,
                                               String app) {
        List<String> features = metadata.apps.find { it.name == app }?.getVisibleFeatures(guidesOption.language) ?: []
        if (guidesOption.language == GROOVY) {
            features.remove 'graalvm'
        }
        features
    }

    private static List<String> expandMacros(List<String> lines, File projectDir) {
        List<String> rawLines = []

        for (String rawLine : lines) {

            if (findInlineMacro(rawLine, 'guideLink:')) {
                rawLine = processGuideLink(rawLine)
            }

            if (rawLine.startsWith(CALLOUT) && rawLine.endsWith(']')) {
                rawLines << callout(rawLine, projectDir)
            } else if (rawLine.startsWith(INCLUDE_COMMONDIR) && rawLine.endsWith('[]')) {
                include rawLine, rawLines, projectDir, true
            } else if (rawLine.startsWith(EXTERNAL) && rawLine.endsWith(']')) {
                include rawLine, rawLines, projectDir, false
            }else if (rawLine.startsWith(COMMON_PARAMETRIZED) && rawLine.endsWith(']')){
                rawLines.addAll includeParametrized(rawLine, projectDir, true)
            } else if (rawLine.startsWith(EXTERNAL_PARAMETRIZED) && rawLine.endsWith(']')){
                rawLines.addAll includeParametrized(rawLine, projectDir, false)
            }
            else {
                rawLines << rawLine
            }
        }
        return rawLines
    }

    private static String callout(String rawLine, File projectDir) {
        String relativePath = parseFileName(rawLine, CALLOUT)
                .orElseThrow(() -> new GradleException("could not parse filename from callout for line: " + rawLine))
        relativePath = 'src/docs/common/callouts/callout-' + relativePath

        File file = new File(projectDir, relativePath)

        List<String> newLines = commonLines(file, projectDir)

        Optional<Integer> number = parseNumber(rawLine)
        String line = number.map(num -> '<' + num + '>').orElse('*') + ' ' + newLines.first()

        for (int i = 0; i < 10; i++) {
            String value = extractFromParametersLine(rawLine, "arg" + i)
            if (value) {
                line = line.replace("{" + i + "}", value)
            }
        }

        line
    }

    private static List<String> includeParametrized(String rawLine, File projectDir, boolean snippet) {

        String prefix = snippet ? COMMON_PARAMETRIZED : EXTERNAL_PARAMETRIZED

        String relativePath = parseFileName(rawLine, prefix)
                .orElseThrow(() -> new GradleException("could not parse filename from include line: " + rawLine))

        if (snippet) {
            relativePath = 'src/docs/common/snippets/common-' + relativePath
        } else {
            relativePath = 'guides/' + relativePath
        }

        File file = new File(projectDir, relativePath)

        List<String> newLines = commonLines(file, projectDir)

        String pattern = "(\\{(\\d+)(:?([UL])?)})"

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern)

        for (int i = 0; i < newLines.size(); i++) {
            String line = newLines[i]
            Matcher m = r.matcher(line)
            while (m.find()) {
                def replaceString = m.group(1)
                def argNum = m.group(2)
                String value = extractFromParametersLine(rawLine, "arg" + argNum)
                def letter = m.group(4)
                if (value) {
                    switch (letter) {
                        case 'U':
                            line = line.replace(replaceString, value.toUpperCase())
                            break
                        case 'L':
                            line = line.replace(replaceString, value.toUpperCase())
                            break
                       default:
                            line = line.replace(replaceString, value)
                           break
                    }
                    newLines[i] = line
                }
            }
        }

        newLines
    }

    private static void include(String rawLine, List<String> rawLines, File projectDir,
                                boolean snippet) {

        String prefix = snippet ? INCLUDE_COMMONDIR : EXTERNAL

        String relativePath = parseFileName(rawLine, prefix)
                .orElseThrow(() -> new GradleException("could not parse filename from include line: " + rawLine))

        if (snippet) {
            relativePath = 'src/docs/common/snippets/common-' + relativePath
        } else {
            relativePath = 'guides/' + relativePath
        }

        File file = new File(projectDir, relativePath)

        rawLines << '// Start: ' + relativePath
        rawLines.addAll commonLines(file, projectDir)
        rawLines << '// End: ' + relativePath
    }

    private static Optional<String> parseFileName(String line, String prefix, String suffix = '.adoc') {
        if (line.contains(prefix) && line.contains('[')) {
            String name = line.substring(line.indexOf(prefix) + prefix.length(), line.indexOf('['))
            if (!name.endsWith(suffix)) {
                name += suffix
            }
            return Optional.of(name)
        }
        Optional.empty()
    }

    private static Optional<Integer> parseNumber(String rawLine) {
        if (rawLine.startsWith(CALLOUT) && rawLine.endsWith(']')) {
            String number = extractFromParametersLine(rawLine, 'number')
            try {
                if (number) {
                    return Optional.of(Integer.valueOf(number))
                }
                return Optional.of(Integer.valueOf(rawLine.substring(rawLine.indexOf('[') + '['.length(), rawLine.indexOf(']'))))
            } catch (NumberFormatException ignored) {
            }
        }
        Optional.empty()
    }

    private static List<String> commonLines(File file, File projectDir) {
        assert file.exists()
        return expandMacros(file.readLines(), projectDir)
    }

    private static boolean shouldProcessLine(String line, String macro) {
        line.startsWith(macro) && line.contains('[') && line.endsWith(']')
    }

    private static boolean findInlineMacro(String line, String macro) {
        if (line.contains(macro)) {
            int indexBracket = line.indexOf('[')
            indexBracket > -1 && line.indexOf(']') > indexBracket
        } else {
            false
        }
    }

    private static String extractName(String line, String macro) {
        line.substring(macro.length(), line.indexOf('['))
    }

    private static List<String> sourceIncludeLines(String slug, String line) {
        sourceIncludeLines(slug, line, null, 'source:')
    }

    private static List<String> testIncludeLines(String slug, String line, TestFramework testFramework) {
        sourceIncludeLines(slug, line, testFramework, 'test:')
    }

    private static List<String> resourceIncludeLines(String slug, String line) {
        resourceIncludeLines(slug, line, 'main', 'resource:')
    }

    private static List<String> testResourceIncludeLines(String slug, String line) {
        resourceIncludeLines(slug, line, 'test', 'testResource:')
    }

    private static List<String> zipIncludeLines(String slug, String line) {
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
                lines << "include::{sourceDir}/$slug/@sourceDir@/${fileName}[${tag}]\n".toString()
            }
        } else {
            lines << "include::{sourceDir}/$slug/@sourceDir@/${fileName}[]".toString()
        }
        lines << '----'
        lines
    }

    private static List<String> sourceIncludeLines(String slug, String line, TestFramework testFramework, String macro) {
        String name = extractName(line, macro)
        String appName = extractAppName(line)
        if (appName == DEFAULT_APP_NAME) {
            appName == ""
        }
        List<String> tagNames = extractTags(line)
        List<String> tags = tagNames ? tagNames.collect { "tag=" + it } : []

        String indent = extractIndent(line)

        String sourcePath = testFramework ? testPath(appName, name, testFramework) : mainPath(appName, name)
        String normalizedSourcePath = (Paths.get(sourcePath)).normalize()
        List<String> lines = [
                '[source,@lang@]',
                '.' + normalizedSourcePath,
                '----',
        ]
        if (tags) {
            for (String tag : tags) {
                String attrs = tag
                if (StringUtils.isNotEmpty(indent)) {
                    attrs += ",${indent}"
                }
                lines << "include::{sourceDir}/$slug/@sourceDir@/${sourcePath}[${attrs}]\n".toString()
            }
        } else {
            lines << "include::{sourceDir}/$slug/@sourceDir@/${sourcePath}[${indent}]".toString()
        }

        lines << '----'
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
                fileName += testFramework == SPOCK ? 'Spec' : 'Test'
            }
        }

        pathByFolder(appName, fileName, 'test')
    }

    @NonNull
    private static String pathByFolder(@NonNull String appName,
                                       @NonNull String fileName,
                                       String folder) {
        String module = appName ? appName + '/' : ''
        "${module}src/${folder}/@lang@/example/micronaut/${fileName}.@languageextension@"
    }

    private static List<String> rawTestIncludeLines(String slug, String line, TestFramework testFramework) {
        String fileName = extractName(line, 'rawTest:')
        String appName = extractAppName(line)
        List<String> tagNames = extractTags(line)

        String module = appName ? appName + '/' : ""
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
                lines.add("include::{sourceDir}/$slug/@sourceDir@/${module}${langTestFolder}/example/micronaut/${fileName}.${fileExtension}[${tag}]\n".toString())
            }
        } else {
            lines.add("include::{sourceDir}/$slug/@sourceDir@/${module}${langTestFolder}/example/micronaut/${fileName}.${fileExtension}[]".toString())
        }

        lines.add('----')
        lines
    }

    private static List<String> resourceIncludeLines(String slug, String line, String resourceDir, String macro) {
        String fileName = extractName(line, macro)
        String appName = extractAppName(line)
        List<String> tagNames = extractTags(line)

        String module = appName ? appName + '/' : ""
        List<String> tags = tagNames ? tagNames.collect { "tag=" + it } : []
        String asciidoctorLang = resolveAsciidoctorLanguage(fileName)

        String pathcallout = fileName.startsWith('../') ? ".${module}src/${resourceDir}/${fileName.substring('../'.length())}" :
                ".${module}src/${resourceDir}/resources/${fileName}"
        List<String> lines = [
                "[source,${asciidoctorLang}]".toString(),
                pathcallout,
                "----",
        ]
        if (tags) {
            for (String tag : tags) {
                lines.add("include::{sourceDir}/$slug/@sourceDir@/${module}src/${resourceDir}/resources/${fileName}[${tag}]\n".toString())
            }
        } else {
            lines.add("include::{sourceDir}/$slug/@sourceDir@/${module}src/${resourceDir}/resources/${fileName}[]".toString())
        }
        lines << '----'
        lines
    }

    private static List<String> includeRocker(String line) {
        String name = extractName(line, 'rocker:')
        String rendered = Rocker.template("io/micronaut/guides/feature/template/${name}.rocker.raw").render()
        return rendered.split("\\r?\\n|\\r") as List
    }

    @NonNull
    private static List<String> featureNames(@NonNull String line,
                                             @NonNull App app,
                                             @NonNull GuidesOption guidesOption) {
        String features = extractFromParametersLine(line, 'features')
        List<String> featureNames
        if (features) {
            featureNames = features.tokenize('|')
        } else {
            featureNames = ([] as List<String>) + app.getFeatures(guidesOption.language)
        }

        String featureExcludes = extractFromParametersLine(line, 'featureExcludes')
        List<String> excludedFeatureNames
        if (featureExcludes) {
            excludedFeatureNames = featureExcludes.tokenize('|')
        } else {
            excludedFeatureNames = []
        }
        featureNames.removeAll excludedFeatureNames

        if (guidesOption.language == GROOVY) {
            featureNames.remove 'graalvm'
        }
        featureNames
    }

    private static String buildDiffLink(String line, GuidesOption guidesOption, GuideMetadata metadata) {

        String appName = extractAppName(line) ?: DEFAULT_APP_NAME
        App app = metadata.apps.find { it.name == appName }
        String link = 'https://micronaut.io/launch?' +
                featureNames(line, app, guidesOption).collect { 'features=' + it }.join('&') +
                '&lang=' + guidesOption.language.name() +
                '&build=' + guidesOption.buildTool.name() +
                '&test=' + guidesOption.testFramework.name() +
                '&name=' + (appName == DEFAULT_APP_NAME ? 'micronautguide' : appName) +
                '&type=' + app.applicationType.name() +
                '&package=example.micronaut' +
                '&activity=diff' +
                '[view the dependency and configuration changes from the specified features, window="_blank"]'

        "NOTE: If you have an existing Micronaut application and want to add the functionality described here, you can " +
                link + " and apply those changes to your application."
    }

    private static String processGuideLink(String line) {
        line.find(GUIDE_LINK_REGEX) { String match, String before, String slug, String text, String after ->
            "${before}link:${slug}.html[$text]$after"
        }
    }

    private static String extractAppName(String line) {
        extractFromParametersLine(line, 'app')
    }

    private static String extractIndent(String line) {
        String indentValue = extractFromParametersLine(line, 'indent')
        indentValue ? "indent=$indentValue" : ""
    }

    private static String extractTagName(String line) {
        extractFromParametersLine(line, 'tag')
    }

    private static List<String> extractTags(String line) {
        String attributeValue = extractFromParametersLine(line, 'tags')
        if (attributeValue) {
            return attributeValue.tokenize('|')
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
