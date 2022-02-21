package io.micronaut.guides

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import io.micronaut.core.util.CollectionUtils
import io.micronaut.guides.tasks.AsciidocGenerationTask
import io.micronaut.guides.tasks.GuidesIndexGradleTask
import io.micronaut.guides.tasks.SampleProjectGenerationTask
import io.micronaut.guides.tasks.TestScriptTask
import io.micronaut.starter.options.BuildTool
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.file.Directory
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.Task
import java.util.stream.Collectors

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
    private static final String KEY_DOC = "doc"
    private static final String COMMA = ","

    @Override
    void apply(Project project) {
        GuideProjectGenerator projectGenerator = new GuideProjectGenerator()
        Directory guidesDir = project.layout.projectDirectory.dir("guides")
        Provider<Directory> codeDir = project.layout.buildDirectory.dir("code")
        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesDir.asFile, "${project.extensions.extraProperties.get("metadataConfigName")}")
        List<Map<String, TaskProvider<Task>>> sampleTasks = metadatas
                .stream()
                .filter(guideMetadata -> Utils.process(guideMetadata, false))
                .map(metadata -> {
                    String taskSlug = kebapCaseToGradleName(metadata.slug)

                    TaskProvider<Copy> githubActionWorkflowTask = registerGenerateGithubActionWorkflow(project,
                            metadata,
                            taskSlug)

                    List<GuidesOption> options = GuideProjectGenerator.guidesOptions(metadata)
                    TaskProvider<SampleProjectGenerationTask> generateTask = registerGenerateTask(project, metadata, projectGenerator, guidesDir, codeDir, taskSlug)
                    TaskProvider<AsciidocGenerationTask> docTask = registerDocTask(project, metadata, guidesDir, generateTask, taskSlug)
                    List<TaskProvider<Zip>> zippers = options.stream()
                            .map(option -> {
                                registerZipTask(project,
                                        metadata,
                                        option,
                                        generateTask)
                            }).collect(Collectors.toList())
                    TaskProvider<Task> zip = registerZipTask(project, taskSlug, zippers)
                    TaskProvider<GuidesIndexGradleTask> indexTask = registerIndexTask(project, taskSlug, metadata)
                    TaskProvider<TestScriptTask> testScriptTask = registerTestScriptTask(project, taskSlug, metadata, generateTask)
                    registerGuideBuild(project, taskSlug, docTask, zip, indexTask, testScriptTask)
                    CollectionUtils.mapOf(KEY_DOC, docTask, KEY_ZIP, zip, KEY_WORKFLOW, githubActionWorkflowTask)
                }).collect(Collectors.toList())

        List<TaskProvider<Task>> docTasks = sampleTasks.stream()
                .map() { Map<String, TaskProvider<Task>> m -> m.get(KEY_DOC) }
                .collect(Collectors.toList())

        TaskProvider<Task> sampleProjects = project.tasks.register("generateSampleProjects") {Task it ->
            it.dependsOn(docTasks)
            it.finalizedBy(FINALIZED_TASKS.stream().map(n -> project.tasks.named(n)).collect(Collectors.toList()))
            it.group = 'guides'
            it.description = 'Generates guide applications at build/code'
        }

        List<TaskProvider<Task>> zipTasks = sampleTasks.stream()
                .map() { Map<String, TaskProvider<Task>> m -> m.get(KEY_ZIP) }
                .collect(Collectors.toList())

        project.tasks.register("generateCodeZip") {Task it ->
            it.group = 'guides'
            it.description = 'Generates a ZIP file for each application at build/code into build/dist'
            it.dependsOn(zipTasks, sampleProjects, 'createDist')
        }

        List<TaskProvider<Task>> workflowTasks = sampleTasks.stream()
                .map() { Map<String, TaskProvider<Task>> m -> m.get(KEY_WORKFLOW) }
                .collect(Collectors.toList())

        project.tasks.register("generateGithubActionWorkflows") {Task it ->
            it.group = 'guides'
            it.description = 'Generates a Github Action Workflow per guide'
            it.dependsOn(workflowTasks)
        }
    }

    /**
     * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onpushpull_requestpull_request_targetpathspaths-ignore
     */
    private static String worflowPaths(GuideMetadata metadata) {
        List<String> paths = [
                'version.txt',
                //'buildSrc/main/java/io/micronaut/features/**',
                //'buildSrc/main/resources/pom.xml',
                "guides/${metadata.slug}/**",
        ] as List<String>
        if (metadata.base) {
            paths.add("guides/${metadata.base}/**".toString())
        }
        String.join(COMMA, paths.stream()
                .map(p -> "\"${p}\"")
                .collect(Collectors.toList()))
    }

    private static String kebapCaseToGradleName(String name) {
        String str = name.split("-")*.capitalize().join("")

        char[] array = str.toCharArray();
        if (array.length > 0) {
            array[0] = Character.toLowerCase(array[0]);
        }
        new String(array);
    }

    private static TaskProvider<Task> registerZipTask(Project project,
                                                      String taskSlug,
                                                      List<TaskProvider<Zip>> zippers) {
        project.tasks.register("${taskSlug}GenerateZips") { Task it ->
            it.group = 'guides'
            it.dependsOn(zippers)
        }
    }

    private static TaskProvider<GuidesIndexGradleTask> registerIndexTask(Project project,
                                                                         String taskSlug,
                                                                         GuideMetadata metadata) {
        project.tasks.register("${taskSlug}Index", GuidesIndexGradleTask) { GuidesIndexGradleTask it ->
            it.group = 'guides'
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
            it.group = 'guides'
            it.metadata = metadata
            it.outputDir.set(project.layout.buildDirectory.dir("code/${metadata.slug}"))
            it.dependsOn(generateTask)
        }
    }

    private static TaskProvider<AsciidocGenerationTask> registerDocTask(Project project,
                                                                        GuideMetadata metadata,
                                                                        Directory guidesDir,
                                                                        TaskProvider<SampleProjectGenerationTask> generateTask,
                                                                        String taskSlug) {
        project.tasks.register("${taskSlug}GenerateDocs", AsciidocGenerationTask) {AsciidocGenerationTask it ->
            it.group = 'guides'
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
        String taskName = "${kebapCaseToGradleName(name)}ZipCode"
        String fromPath = "code/$metadata.slug/$name"
        String archiveFileName = "${name}.zip"
        project.tasks.register(taskName, Zip) {Zip it ->
            it.group = 'guides'
            it.description = "Zips the source project for '${name}'"
            it.dependsOn(generateTask)
            it.from(project.layout.buildDirectory.dir(fromPath))
            it.archiveFileName.set(archiveFileName)
            it.destinationDirectory.set(project.layout.buildDirectory.dir("dist"))
        }
    }

    private static TaskProvider<Copy> registerGenerateGithubActionWorkflow(Project project,
                                                      GuideMetadata metadata,
                                                     String taskSlug) {

        List<GuidesOption> options = GuideProjectGenerator.guidesOptions(metadata)
        String gradleProjects =String.join(COMMA, options
                .stream()
                .filter(option -> option.buildTool.isGradle())
                .map(option -> "\"" + optionName(metadata, option) + "\"" )
                .collect(Collectors.toList()))

        String mavenProjects =String.join(COMMA, options
                .stream()
                .filter(option -> option.buildTool == BuildTool.MAVEN)
                .map(option -> "\"" + optionName(metadata, option) + "\"" )
                .collect(Collectors.toList()))

        project.tasks.register("${taskSlug}GenerateGithubActionWorkflow", Copy) { Copy it ->
            it.from("github-action-template.yml")
            it.into(project.layout.projectDirectory.dir(".github/workflows"))
            it.filter(ReplaceTokens, tokens: [
                    gradleTask: "${taskSlug}${TASK_SUFFIX_GENERATE_PROJECTS}".toString(),
                    javaMatrix: javaMatrix(metadata),
                    slug: metadata.slug,
                    mavenProjects: mavenProjects,
                    gradleProjects: gradleProjects,
                    paths: worflowPaths(metadata)
            ])
            it.rename(new Transformer<String, String>() {
                @Override
                String transform(String s) {
                    "guide-${metadata.slug}.yml"
                }
            })
        }
    }

    private static String javaMatrix(GuideMetadata guideMetadata) {
        String.join(COMMA, (guideMetadata.minimumJavaVersion ? JAVA_MATRIX.stream()
                    .filter(v -> v >= guideMetadata.minimumJavaVersion )
                    .collect(Collectors.toList()) : JAVA_MATRIX)
                .stream()
                .map(v -> "\"${v}\"")
                .collect(Collectors.toList()))
    }


    private static TaskProvider<SampleProjectGenerationTask> registerGenerateTask(Project project,
                                                                           GuideMetadata metadata,
                                                                           GuideProjectGenerator projectGenerator,
                                                                           Directory guidesDir,
                                                                           Provider<Directory> codeDir,
                                                                           String taskSlug) {
        project.tasks.register("${taskSlug}${TASK_SUFFIX_GENERATE_PROJECTS}", SampleProjectGenerationTask) { SampleProjectGenerationTask it ->
            it.group = 'guides'
            it.description = "Generate sample project for guide '${metadata.title}'"
            it.guidesGenerator = projectGenerator
            it.slug.set(metadata.slug)
            it.inputDirectory.set(guidesDir.dir(metadata.slug))
            it.outputDir.set(codeDir.map(s -> s.dir(metadata.slug)))
            it.guidesGenerator = projectGenerator
            it.metadata = metadata
        }
    }

    private static TaskProvider<Task> registerGuideBuild(Project project,
                                      String taskSlug,
                                      TaskProvider<? extends Task>... dependsOnTasks) {
        project.tasks.register("${taskSlug}${TASK_SUFFIX_BUILD}") { Task it ->
            it.group = 'guides' + taskSlug
            it.dependsOn(dependsOnTasks)
        }
    }

    private static final String TASK_SUFFIX_BUILD = "Build";
}
