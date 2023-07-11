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
import static io.micronaut.starter.options.JdkVersion.JDK_17
import static io.micronaut.starter.options.Language.GROOVY
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

@CompileStatic
class GuideProjectGenerator implements AutoCloseable {

    public static final String DEFAULT_APP_NAME = 'default'

    private static final Pattern GROOVY_JAVA_OR_KOTLIN = ~/.*\.java|.*\.groovy|.*\.kt/
    private static final Logger LOG = LoggerFactory.getLogger(this)
    private static final String APP_NAME = 'micronautguide'
    private static final String BASE_PACKAGE = 'example.micronaut'
    private static final List<JdkVersion> JDK_VERSIONS_SUPPORTED_BY_GRAALVM = [JDK_17]

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
    static List<GuideMetadata> parseGuidesMetadata(File guidesDir,
                                                   String metadataConfigName) {
        List<GuideMetadata> metadatas = []

        guidesDir.eachDir { dir ->
            parseGuideMetadata(dir, metadataConfigName).ifPresent(metadatas::add)
        }

        mergeMetadataList(metadatas)

        metadatas
    }

    @CompileDynamic
    static Optional<GuideMetadata> parseGuideMetadata(File dir, String metadataConfigName) {
        File configFile = new File(dir, metadataConfigName)
        if (!configFile.exists()) {
            LOG.warn('metadata file not found for {}', dir.name)
            return Optional.empty()
        }

        Map config = new JsonSlurper().parse(configFile) as Map
        boolean publish = config.publish == null ? true : config.publish

        List<Category> categories = []
        for (String c : config.categories) {
            Category cat = Category.values().find { it.toString() == c }
            if (cat) {
                categories << cat
            } else if (publish && !cat) {
                throw new GradleException("$configFile.parentFile.name metadata.category=$config.category does not exist in Category enum")
            }
        }

        Optional.ofNullable(new GuideMetadata(
                asciidoctor: publish ? dir.name + '.adoc' : null,
                slug: dir.name,
                title: config.title,
                intro: config.intro,
                authors: config.authors,
                tags: config.tags,
                categories: categories,
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
                apps: config.apps.collect { it ->
                    new App(
                            framework: it.framework,
                            testFramework: it.testFramework?.toUpperCase(),
                            name: it.name,
                            visibleFeatures: it.features ?: [],
                            invisibleFeatures: it.invisibleFeatures ?: [],
                            javaFeatures: it.javaFeatures ?: [],
                            kotlinFeatures: it.kotlinFeatures ?: [],
                            groovyFeatures: it.groovyFeatures ?: [],
                            applicationType: it.applicationType ? ApplicationType.valueOf(it.applicationType.toUpperCase()) : ApplicationType.DEFAULT,
                            excludeSource: it.excludeSource,
                            excludeTest: it.excludeTest)
                }
        ))
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

        List<GuideMetadata> metadatas = parseGuidesMetadata(guidesDir, metadataConfigName)
        for (GuideMetadata metadata : metadatas) {
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

    void generateOne(GuideMetadata metadata, File inputDir, File outputDir) {
        if (!outputDir.exists()) {
            assert outputDir.mkdir()
        }

        JdkVersion javaVersion = Utils.parseJdkVersion()
        if (metadata.minimumJavaVersion != null) {
            JdkVersion minimumJavaVersion = JdkVersion.valueOf(metadata.minimumJavaVersion)
            if (minimumJavaVersion.majorVersion() > javaVersion.majorVersion()) {
                javaVersion = minimumJavaVersion
            }
        }

        if (metadata.maximumJavaVersion != null && javaVersion.majorVersion() > metadata.maximumJavaVersion) {
            println "not generating project for $metadata.slug, JDK ${javaVersion.majorVersion()} > $metadata.maximumJavaVersion"
            return
        }

        List<GuidesOption> guidesOptionList = guidesOptions(metadata)
        for (GuidesOption guidesOption : guidesOptionList) {
            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language

            for (App app : metadata.apps) {
                List<String> appFeatures = ([] as List<String>) + app.getFeatures(lang)
                if (guidesOption.language == GROOVY ||
                        !JDK_VERSIONS_SUPPORTED_BY_GRAALVM.contains(javaVersion)) {
                    appFeatures.remove('graalvm')
                }

                if (testFramework == SPOCK) {
                    appFeatures.remove('mockito')
                }

                // typical guides use 'default' as name, multi-project guides have different modules
                String folder = folderName(metadata.slug, guidesOption)

                String appName = app.name == DEFAULT_APP_NAME ? EMPTY_STRING : app.name


                Path destinationPath = Paths.get(outputDir.absolutePath, folder, appName)
                File destination = destinationPath.toFile()
                destination.mkdir()

                String packageAndName = BASE_PACKAGE + '.' + app.name

                guidesGenerator.generateAppIntoDirectory(destination, app.applicationType, packageAndName, app.getFramework(),
                        appFeatures, buildTool, app.testFramework ?: testFramework, lang, javaVersion)

                if (metadata.base) {
                    File baseDir = new File(inputDir.parentFile, metadata.base)
                    copyGuideSourceFiles(baseDir, destinationPath, appName, guidesOption.language.toString(), true)
                }

                copyGuideSourceFiles(inputDir, destinationPath, appName, guidesOption.language.toString())

                if (app.excludeSource) {
                    for (String mainSource : app.excludeSource) {
                        deleteFile(destination, GuideAsciidocGenerator.mainPath(appName, mainSource), guidesOption)
                    }
                }

                if (app.excludeTest) {
                    for (String testSource : app.excludeTest) {
                        deleteFile(destination, GuideAsciidocGenerator.testPath(appName, testSource, testFramework), guidesOption)
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

    static void mergeMetadataList(List<GuideMetadata> metadatas) {
        Map<String, GuideMetadata> metadatasByDirectory = new TreeMap<>()
        for (GuideMetadata metadata : metadatas) {
            metadatasByDirectory[metadata.slug] = metadata
        }

        mergeMetadataMap(metadatasByDirectory)

        metadatas.clear()
        metadatas.addAll metadatasByDirectory.values()
    }

    private static void mergeMetadataMap(Map<String, GuideMetadata> metadatasByDirectory) {
        for (String dir : [] + metadatasByDirectory.keySet()) {
            GuideMetadata metadata = metadatasByDirectory[dir]
            if (metadata.base) {
                GuideMetadata base = metadatasByDirectory[metadata.base]
                GuideMetadata merged = mergeMetadatas(base, metadata)
                metadatasByDirectory[dir] = merged
            }
        }
    }

    private static GuideMetadata mergeMetadatas(GuideMetadata base, GuideMetadata metadata) {
        GuideMetadata merged = new GuideMetadata()
        merged.base = metadata.base
        merged.asciidoctor = metadata.asciidoctor
        merged.slug = metadata.slug
        merged.title = metadata.title ?: base.title
        merged.intro = metadata.intro ?: base.intro
        merged.authors = mergeLists(metadata.authors, base.authors) as Set<String>
        merged.tags = mergeLists(base.tags, metadata.tags)
        merged.categories = metadata.categories ?: base.categories
        merged.publicationDate = metadata.publicationDate
        merged.publish = metadata.publish
        merged.buildTools = metadata.buildTools ?: base.buildTools
        merged.languages = metadata.languages ?: base.languages
        merged.testFramework = metadata.testFramework ?: base.testFramework
        merged.skipGradleTests = base.skipGradleTests || metadata.skipGradleTests
        merged.skipMavenTests = base.skipMavenTests || metadata.skipMavenTests
        merged.minimumJavaVersion = metadata.minimumJavaVersion ?: base.minimumJavaVersion
        merged.maximumJavaVersion = metadata.maximumJavaVersion ?: base.maximumJavaVersion
        merged.zipIncludes = metadata.zipIncludes // TODO support merging from base
        merged.apps = mergeApps(base, metadata)

        merged
    }

    private static List<App> mergeApps(GuideMetadata base, GuideMetadata metadata) {

        Map<String, App> baseApps = base.apps.collectEntries { [(it.name): it] }
        Map<String, App> guideApps = metadata.apps.collectEntries { [(it.name): it] }

        Set<String> baseOnly = baseApps.keySet() - guideApps.keySet()
        Set<String> guideOnly = guideApps.keySet() - baseApps.keySet()
        Collection<String> inBoth = baseApps.keySet().intersect(guideApps.keySet())

        List<App> merged = []
        merged.addAll(baseOnly.collect { baseApps[it] })
        merged.addAll(guideOnly.collect { guideApps[it] })

        for (String name : inBoth) {
            App baseApp = baseApps[name]
            App guideApp = guideApps[name]
            guideApp.visibleFeatures.addAll baseApp.visibleFeatures
            guideApp.invisibleFeatures.addAll baseApp.invisibleFeatures
            guideApp.javaFeatures.addAll baseApp.javaFeatures
            guideApp.kotlinFeatures.addAll baseApp.kotlinFeatures
            guideApp.groovyFeatures.addAll baseApp.groovyFeatures
            merged << guideApp
        }

        merged
    }

    private static List mergeLists(Collection base, Collection others) {
        List merged = []
        if (base) {
            merged.addAll base
        }
        if (others) {
            merged.addAll others
        }
        merged
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
