package io.micronaut.guides

import groovy.json.JsonSlurper
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.util.StringUtils
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

@CompileStatic
class GuideProjectGenerator implements Closeable {
    public static final List<JdkVersion> JDK_VERSIONS_SUPPORTED_BY_GRAALVM = Arrays.asList(JdkVersion.JDK_8, JdkVersion.JDK_11)
    public static final JdkVersion DEFAULT_JAVA_VERSION = JdkVersion.JDK_11
    public static final String DEFAULT_APP_NAME = 'default'
    public static final String ENV_JDK_VERSION = 'JDK_VERSION'
    public static final String SYS_PROP_MICRONAUT_GUIDE = 'micronaut.guide'
    private final ApplicationContext applicationContext
    private final GuidesGenerator guidesGenerator

    String basePackage = 'example.micronaut'
    String appName = 'micronautguide'

    GuideProjectGenerator() {
        applicationContext = ApplicationContext.run()
        guidesGenerator = applicationContext.getBean(GuidesGenerator)
    }

    @Override
    void close() throws IOException {
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
    static GuideMetadata parseGuideMetadata(File dir, String metadataConfigName) {
        File configFile = new File(dir, metadataConfigName)
        if (!configFile.exists()) {
            throw new GradleException("metadata file not found for ${dir.name}")
        }
        def config = new JsonSlurper().parse(configFile)
        Category cat = Category.values().find {it.toString() == config.category }
        if (!cat) {
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
                publicationDate: LocalDate.parse(config.publicationDate),
                languages: config.languages ?: ['java', 'groovy', 'kotlin'],
                buildTools: config.buildTools ?: ['gradle', 'maven'],
                testFramework: config.testFramework,
                skipGradleTests: config.skipGradleTests ?: false,
                skipMavenTests: config.skipMavenTests ?: false,
                apps: config.apps.collect { it -> new App(name: it.name,
                        features: it.features,
                        applicationType: it.applicationType ? ApplicationType.valueOf(it.applicationType.toUpperCase()) : ApplicationType.DEFAULT)
                }
        )
    }

    @CompileDynamic
    void generate(File guidesFolder,
                  File output,
                  String metadataConfigName,
                  boolean merge = true,
                  File asciidocDir = null) {
        guidesFolder.eachDir { dir ->
            GuideMetadata metadata = parseGuideMetadata(dir, metadataConfigName)
            boolean process = System.getProperty(SYS_PROP_MICRONAUT_GUIDE) != null ? System.getProperty(SYS_PROP_MICRONAUT_GUIDE) == metadata.slug : true
            if (process) {
                generate(metadata, dir, output, merge)
                if (asciidocDir != null) {
                    GuideAsciidocGenerator.generate(metadata, dir, asciidocDir)
                }
            }
        }
    }

    static String folderName(String slug, GuidesOption guidesOption) {
        "${slug}-${guidesOption.buildTool.toString()}-${guidesOption.language}"
    }

    static JdkVersion parseJdkVersion() {
        JdkVersion javaVersion = DEFAULT_JAVA_VERSION
        if (System.getenv(ENV_JDK_VERSION)) {
            try {
                int mayorVersion = Integer.valueOf(System.getenv(ENV_JDK_VERSION))
                javaVersion = JdkVersion.valueOf(mayorVersion)
            } catch (NumberFormatException ignored) {
                throw new GradleException("Could not parse env " + ENV_JDK_VERSION + " to JdkVersion")
            }
        }
        javaVersion
    }

    void generate(GuideMetadata metadata, File inputDir, File outputDir, boolean merge = true) {
        String packageAndName = "${basePackage}.${appName}"

        List<GuidesOption> guidesOptionList = guidesOptions(metadata)
        JdkVersion javaVersion = parseJdkVersion()

        for (GuidesOption guidesOption : guidesOptionList) {
            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language

            if (!outputDir.exists()) {
                assert outputDir.mkdir()
            }

            for (App app: metadata.apps) {
                List<String> appFeatures = app.features

                if (guidesOption.language == Language.GROOVY ||
                        !JDK_VERSIONS_SUPPORTED_BY_GRAALVM.contains(parseJdkVersion())) {
                    appFeatures.remove('graalvm')
                }
                // Normal guide use 'default' as name, multi project guides have different modules
                String appName = app.name == DEFAULT_APP_NAME ? StringUtils.EMPTY_STRING : app.name
                String folder = folderName(metadata.slug, guidesOption)

                Path destinationPath = Paths.get(outputDir.absolutePath, folder, appName)
                File destination = destinationPath.toFile()
                destination.mkdir()
                guidesGenerator.generateAppIntoDirectory(destination, app.applicationType, packageAndName, appFeatures, buildTool, testFramework, lang, javaVersion)
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
