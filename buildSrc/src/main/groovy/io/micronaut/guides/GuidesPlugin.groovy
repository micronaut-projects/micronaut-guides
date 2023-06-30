package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.core.util.CollectionUtils
import io.micronaut.guides.GuideMetadata.App
import io.micronaut.guides.tasks.AsciidocGenerationTask
import io.micronaut.guides.tasks.GuidesIndexGradleTask
import io.micronaut.guides.tasks.SampleProjectGenerationTask
import io.micronaut.guides.tasks.TestScriptRunnerTask
import io.micronaut.guides.tasks.TestScriptTask
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.Transformer
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Zip

import java.util.function.Predicate
import java.util.stream.Collectors

import static io.micronaut.guides.GuideProjectGenerator.DEFAULT_APP_NAME
import static io.micronaut.starter.options.BuildTool.MAVEN

@CompileStatic
class GuidesPlugin implements Plugin<Project> {

    private static final String TASK_SUFFIX_GENERATE_PROJECTS = "GenerateProjects"
    private static final List<Integer> JAVA_MATRIX = [8, 11, 17]
    private static final List<String> FINALIZED_TASKS = ['generateTestScript',
                                                         'generateGuidesIndex',
                                                         'generateGuidesJsonMetadata',
                                                         'themeGuides']
    private static final String KEY_ZIP = "zip"
    private static final String KEY_WORKFLOW = "workflow"
    private static final String KEY_WORKFLOW_SNAPSHOT = "workflow-snapshot"
    private static final String TEST_RUNNER = "test-runner"
    private static final String KEY_DOC = "doc"
    private static final String COMMA = ","
    private static final String TASK_SUFFIX_BUILD = "Build"

    @Override
    void apply(Project project) {
        GuideProjectGenerator projectGenerator = new GuideProjectGenerator()
        Directory guidesDir = project.layout.projectDirectory.dir("guides")
        Provider<Directory> codeDir = project.layout.buildDirectory.dir("code")
        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(
                guidesDir.asFile,
                project.extensions.extraProperties.get("metadataConfigName").toString())
        List<Map<String, TaskProvider<Task>>> sampleTasks = metadatas
                .stream()
                .filter(guideMetadata -> Utils.process(guideMetadata, false))
                .map(metadata -> {
                    String taskSlug = kebabCaseToGradleName(metadata.slug)

                    TaskProvider<Copy> githubActionWorkflowTask = registerGenerateGithubActionWorkflow(project,
                            metadata,
                            taskSlug)

                    TaskProvider<Copy> githubActionSnapshotWorkflowTask = registerGenerateGithubActionSnapshotWorkflow(project,
                            metadata,
                            taskSlug)

                    List<GuidesOption> options = GuideProjectGenerator.guidesOptions(metadata)
                    TaskProvider<SampleProjectGenerationTask> generateTask = registerGenerateTask(project, metadata, projectGenerator, guidesDir, codeDir, taskSlug)
                    TaskProvider<AsciidocGenerationTask> docTask = registerDocTask(project, metadata, guidesDir, generateTask, taskSlug)
                    List<TaskProvider<Zip>> zippers = options.stream()
                            .map(option -> {
                                registerZipTask(project, metadata, option, generateTask)
                            }).collect(Collectors.toList())
                    TaskProvider<Task> zip = registerZipTask(project, taskSlug, metadata, zippers)
                    TaskProvider<GuidesIndexGradleTask> indexTask = registerIndexTask(project, taskSlug, metadata)
                    TaskProvider<TestScriptTask> testScriptTask = registerTestScriptTask(project, taskSlug, metadata, generateTask)
                    TaskProvider<TestScriptRunnerTask> testScriptRunnerTask = registerTestScriptRunnerTask(project, taskSlug, metadata, testScriptTask)
                    registerGuideBuild(project, taskSlug, metadata, docTask, zip, indexTask, testScriptTask, testScriptRunnerTask)
                    [(KEY_DOC)              : docTask,
                     (KEY_ZIP)              : zip,
                     (KEY_WORKFLOW)         : githubActionWorkflowTask,
                     (KEY_WORKFLOW_SNAPSHOT): githubActionSnapshotWorkflowTask,
                     (TEST_RUNNER)          : testScriptRunnerTask]
                }).toList() as List<Map<String, TaskProvider<Task>>>

        List<TaskProvider<Task>> docTasks = sampleTasks.stream()
                .map(m -> m.get(KEY_DOC))
                .toList() as List<TaskProvider<Task>>

        TaskProvider<Task> sampleProjects = project.tasks.register("generateSampleProjects") { Task it ->
            it.dependsOn(docTasks)
            it.finalizedBy(FINALIZED_TASKS.stream().map(n -> project.tasks.named(n)).collect(Collectors.toList()))
            it.group = 'guides'
            it.description = 'Generates guide applications at build/code'
        }

        project.tasks.register("runAllGuideTests") { Task it ->
            it.group = 'guides'
            it.description = 'Runs all Guide test scripts'
            it.dependsOn(sampleTasks.stream().map(m -> m.get(TEST_RUNNER)).collect(Collectors.toList()))
        }

        List<TaskProvider<Task>> zipTasks = sampleTasks.stream()
                .map(m -> m.get(KEY_ZIP))
                .toList() as List<TaskProvider<Task>>

        project.tasks.register("generateCodeZip") { Task it ->
            it.group = 'guides'
            it.description = 'Generates a ZIP file for each application at build/code into build/dist'
            it.dependsOn(zipTasks, sampleProjects, 'createDist')
        }

        List<TaskProvider<Task>> workflowTasks = sampleTasks.stream()
                .map(m -> m.get(KEY_WORKFLOW))
                .toList() as List<TaskProvider<Task>>
        workflowTasks.addAll(sampleTasks.stream()
                .map(m -> m.get(KEY_WORKFLOW_SNAPSHOT))
                .toList())

        project.tasks.register("generateGithubActionWorkflows") { Task it ->
            it.group = 'guides'
            it.description = 'Generates a Github Action Workflow per guide'
            it.dependsOn(workflowTasks)
        }
    }

    /**
     * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onpushpull_requestpull_request_targetpathspaths-ignore
     */
    private static String workflowPaths(GuideMetadata metadata) {
        List<String> paths = [
                'version.txt',
                //'buildSrc/main/java/io/micronaut/features/**',
                //'buildSrc/main/resources/pom.xml',
                "guides/" + metadata.slug + "/**",
        ] as List<String>
        if (metadata.base) {
            paths << "guides/" + metadata.base + "/**"
        }
        String.join(COMMA, paths.stream()
                .map(p -> quote(p))
                .collect(Collectors.toList()))
    }

    private static String kebabCaseToGradleName(String name) {
        String str = name.split("-")*.capitalize().join("")

        char[] array = str.toCharArray()
        if (array.length > 0) {
            array[0] = Character.toLowerCase(array[0])
        }
        new String(array)
    }

    private static TaskProvider<Task> registerZipTask(Project project,
                                                      String taskSlug,
                                                      GuideMetadata metadata,
                                                      List<TaskProvider<Zip>> zippers) {
        project.tasks.register("${taskSlug}GenerateZips") { Task it ->
            it.group = "guides $metadata.slug"
            it.dependsOn(zippers)
        }
    }

    private static TaskProvider<GuidesIndexGradleTask> registerIndexTask(Project project,
                                                                         String taskSlug,
                                                                         GuideMetadata metadata) {
        project.tasks.register("${taskSlug}Index", GuidesIndexGradleTask) { GuidesIndexGradleTask it ->
            it.group = "guides $metadata.slug"
            it.description = "Generate index.html for '${metadata.title}'"
            it.metadata = metadata
            it.template.set(project.file("assets/template.html"))
            it.outputDir.set(project.layout.buildDirectory.dir("dist"))
            it.dependsOn(project.tasks.named('createDist'))
        }
    }

    private static TaskProvider<TestScriptTask> registerTestScriptTask(Project project,
                                                                       String taskSlug,
                                                                       GuideMetadata metadata,
                                                                       TaskProvider<SampleProjectGenerationTask> generateTask) {
        project.tasks.register("${taskSlug}TestScript", TestScriptTask) { TestScriptTask it ->
            it.group = "guides $metadata.slug"
            it.description = "Create a test.sh script for the projects generated by $metadata.slug"
            it.metadata = metadata
            it.guideSlug.set(metadata.slug)
            it.metadataFile.set(project.layout.projectDirectory.dir("guides/${metadata.slug}").file("metadata.json"))
            it.scriptFile.set(project.layout.buildDirectory.dir("code/${metadata.slug}").map(d -> d.file("test.sh")))
            it.dependsOn(generateTask)
        }
    }

    private static TaskProvider<TestScriptRunnerTask> registerTestScriptRunnerTask(Project project,
                                                                                   String taskSlug,
                                                                                   GuideMetadata metadata,
                                                                                   TaskProvider<TestScriptTask> testScriptTask) {
        project.tasks.register("${taskSlug}RunTestScript", TestScriptRunnerTask) { TestScriptRunnerTask it ->
            it.onlyIf { !Utils.skipBecauseOfJavaVersion(metadata) }

            Provider<Directory> codeDirectory = project.layout.buildDirectory.dir("code/${metadata.slug}")

            it.group = "guides $metadata.slug"
            it.description = "Run the tests for all projects generated by $metadata.slug"

            it.testScript.set(testScriptTask.flatMap { t -> t.scriptFile })
            it.guideSourceDirectory.set(project.layout.projectDirectory.dir("guides/${metadata.slug}"))

            // We tee the script output to a file, this is the cached result
            it.outputFile.set(codeDirectory.map(d -> d.file("output.log")))
        }
    }

    private static TaskProvider<AsciidocGenerationTask> registerDocTask(Project project,
                                                                        GuideMetadata metadata,
                                                                        Directory guidesDir,
                                                                        TaskProvider<SampleProjectGenerationTask> generateTask,
                                                                        String taskSlug) {
        project.tasks.register("${taskSlug}GenerateDocs", AsciidocGenerationTask) { AsciidocGenerationTask it ->
            it.group = "guides $metadata.slug"
            it.dependsOn(generateTask)
            it.description = "Generate asciidoc files for '${metadata.title}'"
            it.slug.set(metadata.slug)
            it.inputDirectory.set(guidesDir.dir(metadata.slug))
            it.outputDir.set(project.layout.projectDirectory.dir("src/docs/asciidoc"))
            it.metadata = metadata
        }
    }

    private static String optionName(GuideMetadata metadata, GuidesOption option) {
        "${metadata.slug}-${option.buildTool}-${option.language}"
    }

    private static TaskProvider<Zip> registerZipTask(Project project,
                                                     GuideMetadata metadata,
                                                     GuidesOption option,
                                                     TaskProvider<SampleProjectGenerationTask> generateTask) {
        String name = optionName(metadata, option)
        String taskName = "${kebabCaseToGradleName(name)}ZipCode"
        String fromPath = "code/$metadata.slug/$name"
        String archiveFileName = "${name}.zip"
        project.tasks.register(taskName, Zip) { Zip it ->
            it.group = "guides $metadata.slug"
            it.description = "Zips the source project for '${name}'"
            it.dependsOn(generateTask)
            it.from(project.layout.buildDirectory.dir(fromPath))
            it.archiveFileName.set(archiveFileName)
            it.destinationDirectory.set(project.layout.buildDirectory.dir("dist"))
        }
    }

    private static Map<String, Object> workflowTokens(GuideMetadata metadata,
                                                      String taskSlug) {
        List<GuidesOption> options = GuideProjectGenerator.guidesOptions(metadata)

        List<GuidesOption> gradleOptions = options
                .stream()
                .filter(option -> option.buildTool.isGradle())
                .collect(Collectors.toList())

        List<GuidesOption> mavenOptions = options
                .stream()
                .filter(option -> option.buildTool == MAVEN)
                .collect(Collectors.toList())

        String gradleProjects = projects(metadata, gradleOptions)

        String mavenProjects = projects(metadata, mavenOptions)

        boolean mavenEnabled = !(CollectionUtils.isEmpty(mavenOptions) || metadata.skipMavenTests)
        boolean gradleEnabled = !(CollectionUtils.isEmpty(gradleOptions) || metadata.skipGradleTests)
        [
                gradleTask    : taskSlug + TASK_SUFFIX_GENERATE_PROJECTS,
                javaMatrix    : javaMatrix(metadata),
                slug          : metadata.slug,
                mavenProjects : mavenProjects,
                gradleProjects: gradleProjects,
                paths         : workflowPaths(metadata),
                mavenEnabled  : String.valueOf(mavenEnabled),
                gradleEnabled : String.valueOf(gradleEnabled)
        ] as Map
    }

    private static String projects(GuideMetadata metadata, List<GuidesOption> options) {

        List<String> combinations = options
                .stream()
                .map(option -> optionName(metadata, option))
                .collect(Collectors.toList())

        List<String> allCombinations = []

        for (String combination : combinations) {
            for (App app : metadata.apps) {
                if (DEFAULT_APP_NAME.equals(app.name)) {
                    allCombinations << quote(combination)
                } else {
                    allCombinations << quote(combination + '/' + app.name)
                }
            }
        }

        String.join(COMMA, allCombinations)
    }

    private static TaskProvider<Copy> registerGenerateGithubActionSnapshotWorkflow(Project project,
                                                                                   GuideMetadata metadata,
                                                                                   String taskSlug) {
        Map<String, Object> tokens = workflowTokens(metadata, taskSlug)
        tokens.workflowName = "Test " + metadata.slug + " Snapshot"
        project.tasks.register("${taskSlug}GenerateGithubActionSnapshotWorkflow", Copy) { Copy it ->
            it.from("github-action-snapshot-template.yml")
            it.into(project.layout.projectDirectory.dir(".github/workflows"))
            it.filter(ReplaceTokens, tokens: tokens)
            it.rename(new Transformer<String, String>() {
                @Override
                String transform(String s) {
                    "guide-${metadata.slug}-snapshot.yml"
                }
            })
        }
    }

    private static TaskProvider<Copy> registerGenerateGithubActionWorkflow(Project project,
                                                                           GuideMetadata metadata,
                                                                           String taskSlug) {
        Map<String, Object> tokens = workflowTokens(metadata, taskSlug)
        tokens.workflowName = "Test " + metadata.slug
        project.tasks.register("${taskSlug}GenerateGithubActionWorkflow", Copy) { Copy it ->
            it.from("github-action-template.yml")
            it.into(project.layout.projectDirectory.dir(".github/workflows"))
            it.filter(ReplaceTokens, tokens: tokens)
            it.rename(new Transformer<String, String>() {
                @Override
                String transform(String s) {
                    "guide-${metadata.slug}.yml"
                }
            })
        }
    }

    private static String javaMatrix(GuideMetadata guideMetadata) {
        String.join(COMMA, JAVA_MATRIX
                .stream()
                .filter(minFilter(guideMetadata))
                .filter(maxFilter(guideMetadata))
                .map(v -> quote(v))
                .collect(Collectors.toList()))
    }

    private static Predicate<Integer> minFilter(GuideMetadata guideMetadata) {
        (guideMetadata.minimumJavaVersion ? { int v -> v >= guideMetadata.minimumJavaVersion } : { true }) as Predicate
    }

    private static Predicate<Integer> maxFilter(GuideMetadata guideMetadata) {
        (guideMetadata.maximumJavaVersion ? { int v -> v <= guideMetadata.maximumJavaVersion } : { true }) as Predicate
    }

    private static TaskProvider<SampleProjectGenerationTask> registerGenerateTask(Project project,
                                                                                  GuideMetadata metadata,
                                                                                  GuideProjectGenerator projectGenerator,
                                                                                  Directory guidesDir,
                                                                                  Provider<Directory> codeDir,
                                                                                  String taskSlug) {
        project.tasks.register("${taskSlug}${TASK_SUFFIX_GENERATE_PROJECTS}", SampleProjectGenerationTask) { SampleProjectGenerationTask it ->
            it.group = "guides $metadata.slug"
            it.description = "Generate sample project for guide '${metadata.title}'"
            it.guidesGenerator = projectGenerator
            it.slug.set(metadata.slug)
            it.inputDirectory.set(guidesDir.dir(metadata.slug))
            if (metadata.base != null) {
                it.baseInputDirectory.set(guidesDir.dir(metadata.base))
            }
            it.outputDir.set(codeDir.map(s -> s.dir(metadata.slug)))
            it.guidesGenerator = projectGenerator
            it.metadata = metadata
        }
    }

    private static TaskProvider<Task> registerGuideBuild(Project project,
                                                         String taskSlug,
                                                         GuideMetadata metadata,
                                                         TaskProvider<? extends Task>... dependsOnTasks) {
        project.tasks.register("${taskSlug}${TASK_SUFFIX_BUILD}") { Task it ->
            it.group = "guides $metadata.slug"
            it.dependsOn(dependsOnTasks)
            it.finalizedBy(project.tasks.named('asciidoctor'), project.tasks.named('themeGuides'))
        }
    }

    private static String quote(it) {
        '"' + it + '"'
    }
}
