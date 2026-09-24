package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.core.util.CollectionUtils
import io.micronaut.guides.core.DefaultGuideParser
import io.micronaut.guides.core.DefaultJsonSchemaProvider
import io.micronaut.guides.core.Guide
import io.micronaut.guides.core.GuideParser
import io.micronaut.guides.core.GuidesOption
import io.micronaut.guides.core.JsonSchemaProvider
import io.micronaut.guides.tasks.AsciidocGenerationTask
import io.micronaut.guides.tasks.GuidesIndexGradleTask
import io.micronaut.guides.tasks.SampleProjectGenerationTask
import io.micronaut.guides.tasks.TestScriptRunnerTask
import io.micronaut.guides.tasks.TestScriptTask
import io.micronaut.guides.tasks.NativeTestScriptRunnerTask
import io.micronaut.guides.tasks.NativeTestScriptTask
import io.micronaut.guides.tasks.PythonTestScriptTask
import io.micronaut.json.JsonMapper
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.Transformer
import org.gradle.api.file.Directory
import org.gradle.api.initialization.IncludedBuild
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Zip

import java.math.RoundingMode
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Predicate
import java.util.stream.Collectors

import static io.micronaut.guides.GuideProjectGenerator.DEFAULT_APP_NAME
import static io.micronaut.starter.options.BuildTool.MAVEN
import static io.micronaut.starter.options.BuildTool.PYRONAUT
import static io.micronaut.starter.options.Language.PYTHON

@CompileStatic
class GuidesPlugin implements Plugin<Project> {

    private static final String TASK_SUFFIX_GENERATE_PROJECTS = "GenerateProjects"
    private static final List<Integer> JAVA_MATRIX = [21]
    private static final List<String> FINALIZED_TASKS = ['generateTestScript',
                                                         'generateGuidesIndex',
                                                         'generateGuidesJsonMetadata',
                                                         'themeGuides']
    private static final String KEY_ZIP = "zip"
    private static final String KEY_WORKFLOW = "workflow"
    private static final String KEY_WORKFLOW_SNAPSHOT = "workflow-snapshot"
    private static final String TEST_RUNNER = "test-runner"
    private static final String PYTHON_TEST_SCRIPT = "python-test-script"
    private static final String PYTHON_TEST_RUNNER = "python-test-runner"
    private static final String KEY_DOC = "doc"
    private static final String COMMA = ","
    private static final String TASK_SUFFIX_BUILD = "Build"
    private static final String LOCAL_GIT_PYRONAUT_PROPERTY = "local.git.pyronaut"
    private static final String LOCAL_GIT_PYRONAUT_ENV = "LOCAL_GIT_PYRONAUT"
    private static final String LOCAL_PYRONAUT_CORE_VERSION_PROPERTY = "local.pyronaut.core.version"
    private static final String LOCAL_PYRONAUT_CORE_VERSION_ENV = "LOCAL_PYRONAUT_CORE_VERSION"
    private static final String LOCAL_PYRONAUT_PLATFORM_VERSION_PROPERTY = "local.pyronaut.platform.version"
    private static final String LOCAL_PYRONAUT_PLATFORM_VERSION_ENV = "LOCAL_PYRONAUT_PLATFORM_VERSION"
    private static final String DEFAULT_LOCAL_PYRONAUT_CORE_VERSION = "5.2.3"
    private static final String DEFAULT_LOCAL_PYRONAUT_PLATFORM_VERSION = "5.1.0"
    private static final String PYRONAUT_INCLUDED_BUILD_NAME = "pyronaut"
    private static final String PYRONAUT_FIXTURE_REPOSITORY = "functional-test/build/fixture-repo"
    private static final String PYRONAUT_INSTALL_EXECUTABLE = "pyronaut-install/build/install/micronaut-pyronaut-install/bin/pyronaut-install"
    private static final String PYRONAUT_VALIDATE_CONFIG_EXECUTABLE = "pyronaut-validate-config/build/install/micronaut-pyronaut-validate-config/bin/pyronaut-validate-config"
    private static final String PYRONAUT_PROCESS_EXECUTABLE = "pyronaut-processor/build/install/micronaut-pyronaut-processor/bin/pyronaut-processor"
    private static final String PYRONAUT_TEST_EXECUTABLE = "pyronaut-test/build/install/micronaut-pyronaut-test/bin/pyronaut-test"
    private static final String PYRONAUT_TEST_RESOURCES_SERVER_EXECUTABLE = "pyronaut-test-resources-server/build/install/micronaut-pyronaut-test-resources-server/bin/pyronaut-test-resources-server"
    private static final String PYRONAUT_CLI_PYTHONPATH = "pyronaut/src/main/python"
    private static final String PYRONAUT_FIXTURE_LAUNCHER_TASK = ":micronaut-functional-test:installFixtureLaunchers"
    private static final List<String> PYRONAUT_FIXTURE_STAGE_TASKS = List.of(
            ":micronaut-functional-test:stagePyronautFixtureArtifacts",
            ":micronaut-functional-test:stageMicronautPlatformFixtureArtifact",
            ":micronaut-functional-test:stageMicronautCoreFixtureArtifacts",
            ":micronaut-functional-test:stageMicronautDataFixtureArtifacts",
            ":micronaut-functional-test:stageSourcegenFixtureArtifacts",
            ":micronaut-functional-test:stageIncludedCoreExternalFixtureArtifacts",
            ":micronaut-functional-test:stageMicronautTestFixtureArtifacts"
    )

    @Override
    void apply(Project project) {
        GuideProjectGenerator projectGenerator = new GuideProjectGenerator()
        Directory guidesDir = project.layout.projectDirectory.dir("guides")
        Provider<Directory> codeDir = project.layout.buildDirectory.dir("code")
        Provider<String> localPyronautPath = localGitPath(project, LOCAL_GIT_PYRONAUT_PROPERTY, LOCAL_GIT_PYRONAUT_ENV)
        Provider<String> localPyronautCoreVersion = configuredValue(project, LOCAL_PYRONAUT_CORE_VERSION_PROPERTY, LOCAL_PYRONAUT_CORE_VERSION_ENV, DEFAULT_LOCAL_PYRONAUT_CORE_VERSION)
        Provider<String> localPyronautPlatformVersion = configuredValue(project, LOCAL_PYRONAUT_PLATFORM_VERSION_PROPERTY, LOCAL_PYRONAUT_PLATFORM_VERSION_ENV, DEFAULT_LOCAL_PYRONAUT_PLATFORM_VERSION)
        Provider<String> localPyronautRepository = localPyronautPath.map(path -> new File(path, PYRONAUT_FIXTURE_REPOSITORY).absolutePath)
        Provider<String> localPyronautInstallExecutable = localPyronautPath.map(path -> new File(path, PYRONAUT_INSTALL_EXECUTABLE).absolutePath)
        Provider<String> localPyronautValidateConfigExecutable = localPyronautPath.map(path -> new File(path, PYRONAUT_VALIDATE_CONFIG_EXECUTABLE).absolutePath)
        Provider<String> localPyronautProcessExecutable = localPyronautPath.map(path -> new File(path, PYRONAUT_PROCESS_EXECUTABLE).absolutePath)
        Provider<String> localPyronautTestExecutable = localPyronautPath.map(path -> new File(path, PYRONAUT_TEST_EXECUTABLE).absolutePath)
        Provider<String> localPyronautTestResourcesServerExecutable = localPyronautPath.map(path -> new File(path, PYRONAUT_TEST_RESOURCES_SERVER_EXECUTABLE).absolutePath)
        Provider<String> localPyronautCliPythonPath = localPyronautPath.map(path -> new File(path, PYRONAUT_CLI_PYTHONPATH).absolutePath)
        TaskProvider<Task> stageLocalPyronautArtifactsTask = registerStageLocalPyronautArtifactsTask(project, localPyronautRepository)
        Properties testProps = guidesDir.file("tests.properties").asFile.withInputStream { inputStream ->
            new Properties().tap {
                load(inputStream)
            }
        } as Properties

        JsonMapper jsonMapper = JsonMapper.createDefault();
        JsonSchemaProvider jsonSchemaProvider = new DefaultJsonSchemaProvider();
        GuideParser guideParser = new DefaultGuideParser(jsonSchemaProvider, jsonMapper);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(
                guidesDir.asFile,
                project.extensions.extraProperties.get("metadataConfigName").toString())
        List<Map<String, TaskProvider<Task>>> sampleTasks = metadatas
                .stream()
                .filter(guideMetadata -> Utils.process(guideMetadata, false))
                .map(metadata -> {
                    String taskSlug = kebabCaseToGradleName(metadata.slug())

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
                    TaskProvider<PythonTestScriptTask> pythonTestScriptTask = hasPythonOption(options) ? registerPythonTestScriptTask(project, taskSlug, metadata, generateTask) : null
                    TaskProvider<TestScriptRunnerTask> pythonTestScriptRunnerTask = pythonTestScriptTask ? registerPythonTestScriptRunnerTask(
                            project,
                            taskSlug,
                            metadata,
                            pythonTestScriptTask,
                            stageLocalPyronautArtifactsTask,
                            localPyronautRepository,
                            localPyronautCoreVersion,
                            localPyronautPlatformVersion,
                            localPyronautInstallExecutable,
                            localPyronautValidateConfigExecutable,
                            localPyronautProcessExecutable,
                            localPyronautTestExecutable,
                            localPyronautTestResourcesServerExecutable,
                            localPyronautCliPythonPath
                    ) : null
                    TaskProvider<NativeTestScriptTask> nativeTestScriptTask = registerNativeTestScriptTask(project, taskSlug, metadata, generateTask)
                    TaskProvider<NativeTestScriptRunnerTask> nativeTestScriptRunnerTask = registerNativeTestScriptRunnerTask(project, taskSlug, metadata, nativeTestScriptTask)

                    List<TaskProvider<? extends Task>> guideBuildTasks = [docTask, zip, indexTask, testScriptTask, testScriptRunnerTask, nativeTestScriptTask, nativeTestScriptRunnerTask]
                    if (pythonTestScriptTask && pythonTestScriptRunnerTask) {
                        guideBuildTasks.add(pythonTestScriptTask)
                        guideBuildTasks.add(pythonTestScriptRunnerTask)
                    }
                    registerGuideBuild(project, taskSlug, metadata, guideBuildTasks)
                    [(KEY_DOC)              : docTask,
                     (KEY_ZIP)              : zip,
                     (KEY_WORKFLOW)         : githubActionWorkflowTask,
                     (KEY_WORKFLOW_SNAPSHOT): githubActionSnapshotWorkflowTask,
                     (TEST_RUNNER)          : testScriptRunnerTask,
                     (PYTHON_TEST_SCRIPT)   : pythonTestScriptTask,
                     (PYTHON_TEST_RUNNER)   : pythonTestScriptRunnerTask]
                }).toList() as List<Map<String, TaskProvider<Task>>>

        List<TaskProvider<Task>> docTasks = sampleTasks.stream()
                .map(m -> m.get(KEY_DOC))
                .toList() as List<TaskProvider<Task>>

        project.tasks.named("asciidoctor").configure { Task it ->
            it.mustRunAfter(docTasks)
        }

        TaskProvider<Task> sampleProjects = project.tasks.register("generateSampleProjects") { Task it ->
            it.dependsOn(docTasks)
            it.finalizedBy(FINALIZED_TASKS.stream().map(n -> project.tasks.named(n)).collect(Collectors.toList()))
            it.group = 'guides'
            it.description = 'Generates guide applications at build/code'
        }

        int groupSize = (sampleTasks.size() / Integer.parseInt(testProps.get("numberOfTestGroups") as String))
                .setScale(0, RoundingMode.UP).toInteger()

        sampleTasks.collate(groupSize, true).eachWithIndex { List<Map<String, TaskProvider<Task>>> tasks, int i ->
            project.tasks.register("testsGroup${i + 1}") { Task it ->
                it.group = 'guides'
                it.description = "Run group of guide tests"
                it.dependsOn(tasks.collect { it[TEST_RUNNER] })
            }
        }

        project.tasks.register("runAllGuideTests") { Task it ->
            it.group = 'guides'
            it.description = 'Runs all Guide test scripts'
            it.dependsOn(sampleTasks.stream().map(m -> m.get(TEST_RUNNER)).collect(Collectors.toList()))
        }

        List<TaskProvider<Task>> pythonTestRunnerTasks = sampleTasks.stream()
                .map(m -> m.get(PYTHON_TEST_RUNNER))
                .filter(Objects::nonNull)
                .toList() as List<TaskProvider<Task>>

        if (!pythonTestRunnerTasks.isEmpty()) {
            int pythonGroupSize = (pythonTestRunnerTasks.size() / Integer.parseInt(testProps.get("numberOfTestGroups") as String))
                    .setScale(0, RoundingMode.UP).toInteger()
            pythonTestRunnerTasks.collate(pythonGroupSize, true).eachWithIndex { List<TaskProvider<Task>> tasks, int i ->
                project.tasks.register("pythonTestsGroup${i + 1}") { Task it ->
                    it.group = 'guides'
                    it.description = "Run group of Python guide tests"
                    it.dependsOn(tasks)
                }
            }
        }

        project.tasks.register("runAllPythonGuideTests") { Task it ->
            it.group = 'guides'
            it.description = 'Runs all Python Guide test scripts'
            it.dependsOn(pythonTestRunnerTasks)
        }

        List<TaskProvider<Task>> pythonTestScriptTasks = sampleTasks.stream()
                .map(m -> m.get(PYTHON_TEST_SCRIPT))
                .filter(Objects::nonNull)
                .toList() as List<TaskProvider<Task>>

        project.tasks.register("generateAllPythonGuideTestScripts") { Task it ->
            it.group = 'guides'
            it.description = 'Generates every Python guide project and test script without running Pyronaut'
            it.dependsOn(pythonTestScriptTasks)
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

    private static Provider<String> localGitPath(Project project,
                                                 String propertyName,
                                                 String environmentName) {
        project.providers.gradleProperty(propertyName)
                .orElse(project.providers.environmentVariable(environmentName))
    }

    private static Provider<String> configuredValue(Project project,
                                                    String propertyName,
                                                    String environmentName,
                                                    String defaultValue) {
        project.providers.gradleProperty(propertyName)
                .orElse(project.providers.environmentVariable(environmentName))
                .orElse(defaultValue)
    }

    private static TaskProvider<Task> registerStageLocalPyronautArtifactsTask(Project project,
                                                                              Provider<String> localPyronautRepository) {
        IncludedBuild includedBuild = project.gradle.includedBuilds.find { IncludedBuild build ->
            build.name == PYRONAUT_INCLUDED_BUILD_NAME
        }
        project.tasks.register("stageLocalPyronautArtifacts") { Task it ->
            it.group = "build setup"
            it.description = "Stages Pyronaut artifacts from the local included Pyronaut checkout into its fixture repository."
            it.outputs.dir(localPyronautRepository.map(path -> new File(path)))
            if (includedBuild != null) {
                it.dependsOn(PYRONAUT_FIXTURE_STAGE_TASKS.collect { String taskPath -> includedBuild.task(taskPath) })
                it.dependsOn(includedBuild.task(PYRONAUT_FIXTURE_LAUNCHER_TASK))
            } else {
                it.doFirst {
                    throw new GradleException("Python guide tests require an included '${PYRONAUT_INCLUDED_BUILD_NAME}' build. Configure ${LOCAL_GIT_PYRONAUT_PROPERTY} or ${LOCAL_GIT_PYRONAUT_ENV}.")
                }
            }
        }
    }

    /**
     * https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#onpushpull_requestpull_request_targetpathspaths-ignore
     */
    private static String workflowPaths(Guide metadata) {
        List<String> paths = [
                'version.txt',
                //'buildSrc/main/java/io/micronaut/features/**',
                //'buildSrc/main/resources/pom.xml',
                "guides/" + metadata.slug() + "/**",
        ] as List<String>
        if (metadata.base()) {
            paths << "guides/" + metadata.base() + "/**"
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
                                                      Guide metadata,
                                                      List<TaskProvider<Zip>> zippers) {
        project.tasks.register("${taskSlug}GenerateZips") { Task it ->
            it.group = "guides ${metadata.slug()}"
            it.dependsOn(zippers)
        }
    }

    private static TaskProvider<GuidesIndexGradleTask> registerIndexTask(Project project,
                                                                         String taskSlug,
                                                                         Guide metadata) {
        project.tasks.register("${taskSlug}Index", GuidesIndexGradleTask) { GuidesIndexGradleTask it ->
            it.group = "guides ${metadata.slug()}"
            it.description = "Generate index.html for '${metadata.title()}'"
            it.metadata = metadata
            it.template.set(project.file("assets/template.html"))
            it.outputDir.set(project.layout.buildDirectory.dir("dist"))
            it.dependsOn(project.tasks.named('createDist'))
        }
    }

    private static TaskProvider<TestScriptTask> registerTestScriptTask(Project project,
                                                                       String taskSlug,
                                                                       Guide metadata,
                                                                       TaskProvider<SampleProjectGenerationTask> generateTask) {
        project.tasks.register("${taskSlug}TestScript", TestScriptTask) { TestScriptTask it ->
            it.group = "guides ${metadata.slug()}"
            it.description = "Create a test.sh script for the projects generated by ${metadata.slug()}"
            it.metadata = metadata
            it.guideSlug.set(metadata.slug())
            it.metadataFile.set(project.layout.projectDirectory.dir("guides/${metadata.slug()}").file("metadata.json"))
            it.scriptFile.set(project.layout.buildDirectory.dir("code/${metadata.slug()}").map(d -> d.file("test.sh")))
            it.dependsOn(generateTask)
        }
    }

    private static TaskProvider<PythonTestScriptTask> registerPythonTestScriptTask(Project project,
                                                                                   String taskSlug,
                                                                                   Guide metadata,
                                                                                   TaskProvider<SampleProjectGenerationTask> generateTask) {
        project.tasks.register("${taskSlug}PythonTestScript", PythonTestScriptTask) { PythonTestScriptTask it ->
            it.group = "guides ${metadata.slug()}"
            it.description = "Create a python-test.sh script for the Pyronaut project generated by ${metadata.slug()}"
            it.metadata = metadata
            it.guideSlug.set(metadata.slug())
            it.metadataFile.set(project.layout.projectDirectory.dir("guides/${metadata.slug()}").file("metadata.json"))
            it.scriptFile.set(project.layout.buildDirectory.dir("code/${metadata.slug()}").map(d -> d.file("python-test.sh")))
            it.dependsOn(generateTask)
        }
    }

    private static TaskProvider<NativeTestScriptTask> registerNativeTestScriptTask(Project project,
                                                                       String taskSlug,
                                                                       Guide metadata,
                                                                       TaskProvider<SampleProjectGenerationTask> generateTask) {
        project.tasks.register("${taskSlug}NativeTestScript", NativeTestScriptTask) { NativeTestScriptTask it ->
            it.group = "guides ${metadata.slug()}"
            it.description = "Create a native-test.sh script for the projects generated by ${metadata.slug()}"
            it.metadata = metadata
            it.guideSlug.set(metadata.slug())
            it.metadataFile.set(project.layout.projectDirectory.dir("guides/${metadata.slug()}").file("metadata.json"))
            it.scriptFile.set(project.layout.buildDirectory.dir("code/${metadata.slug()}").map(d -> d.file("native-test.sh")))
            it.dependsOn(generateTask)
            it.enabled = isGraalVMJava()
        }
    }

    private static TaskProvider<NativeTestScriptRunnerTask> registerNativeTestScriptRunnerTask(Project project,
                                                                                               String taskSlug,
                                                                                               Guide metadata,
                                                                                               TaskProvider<NativeTestScriptTask> nativeTestScriptTask) {
        project.tasks.register("${taskSlug}RunNativeTestScript", NativeTestScriptRunnerTask) { NativeTestScriptRunnerTask it ->
            it.onlyIf { !Utils.skipBecauseOfJavaVersion(metadata) }

            Provider<Directory> codeDirectory = project.layout.buildDirectory.dir("code/${metadata.slug()}")

            it.group = "guides ${metadata.slug()}"
            it.description = "Run the native tests for all projects generated by ${metadata.slug()}"

            it.nativeTestScript.set(nativeTestScriptTask.flatMap { t -> t.scriptFile })
            it.guideSourceDirectory.set(project.layout.projectDirectory.dir("guides/${metadata.slug()}"))

            // We tee the script output to a file, this is the cached result
            it.outputFile.set(codeDirectory.map(d -> d.file("output.log")))
            it.enabled = isGraalVMJava()
        }
    }

    private static TaskProvider<TestScriptRunnerTask> registerTestScriptRunnerTask(Project project,
                                                                                   String taskSlug,
                                                                                   Guide metadata,
                                                                                   TaskProvider<TestScriptTask> testScriptTask) {
        project.tasks.register("${taskSlug}RunTestScript", TestScriptRunnerTask) { TestScriptRunnerTask it ->
            it.onlyIf { !Utils.skipBecauseOfJavaVersion(metadata) }

            Provider<Directory> codeDirectory = project.layout.buildDirectory.dir("code/${metadata.slug()}")

            it.group = "guides ${metadata.slug()}"
            it.description = "Run the tests for all projects generated by ${metadata.slug()}"

            it.environment.set(metadata.env())

            it.testScript.set(testScriptTask.flatMap { t -> t.scriptFile })
            it.guideSourceDirectory.set(project.layout.projectDirectory.dir("guides/${metadata.slug()}"))

            // We tee the script output to a file, this is the cached result
            it.outputFile.set(codeDirectory.map(d -> d.file("output.log")))
        }
    }

    private static TaskProvider<TestScriptRunnerTask> registerPythonTestScriptRunnerTask(Project project,
                                                                                        String taskSlug,
                                                                                        Guide metadata,
                                                                                        TaskProvider<PythonTestScriptTask> pythonTestScriptTask,
                                                                                        TaskProvider<Task> stageLocalPyronautArtifactsTask,
                                                                                        Provider<String> localPyronautRepository,
                                                                                        Provider<String> localPyronautCoreVersion,
                                                                                        Provider<String> localPyronautPlatformVersion,
                                                                                        Provider<String> localPyronautInstallExecutable,
                                                                                        Provider<String> localPyronautValidateConfigExecutable,
                                                                                        Provider<String> localPyronautProcessExecutable,
                                                                                        Provider<String> localPyronautTestExecutable,
                                                                                        Provider<String> localPyronautTestResourcesServerExecutable,
                                                                                        Provider<String> localPyronautCliPythonPath) {
        project.tasks.register("${taskSlug}RunPythonTestScript", TestScriptRunnerTask) { TestScriptRunnerTask it ->
            it.onlyIf { !Utils.skipBecauseOfJavaVersion(metadata) }

            Provider<Directory> codeDirectory = project.layout.buildDirectory.dir("code/${metadata.slug()}")

            it.group = "guides ${metadata.slug()}"
            it.description = "Run the Python tests for the Pyronaut project generated by ${metadata.slug()}"

            it.environment.set(metadata.env())
            it.environment.put("PYRONAUT_LOCAL_REPOSITORY", localPyronautRepository)
            it.environment.put("PYRONAUT_LOCAL_CORE_VERSION", localPyronautCoreVersion)
            it.environment.put("PYRONAUT_LOCAL_PLATFORM_VERSION", localPyronautPlatformVersion)
            it.environment.put("PYRONAUT_INSTALL_EXECUTABLE", localPyronautInstallExecutable)
            it.environment.put("PYRONAUT_VALIDATE_CONFIG_EXECUTABLE", localPyronautValidateConfigExecutable)
            it.environment.put("PYRONAUT_PROCESS_EXECUTABLE", localPyronautProcessExecutable)
            it.environment.put("PYRONAUT_PROCESSOR_EXECUTABLE", localPyronautProcessExecutable)
            it.environment.put("PYRONAUT_TEST_EXECUTABLE", localPyronautTestExecutable)
            it.environment.put("PYRONAUT_TEST_RESOURCES_SERVER_EXECUTABLE", localPyronautTestResourcesServerExecutable)
            it.environment.put("PYRONAUT_CLI_PYTHONPATH", localPyronautCliPythonPath)
            it.testScript.set(pythonTestScriptTask.flatMap { t -> t.scriptFile })
            it.guideSourceDirectory.set(project.layout.projectDirectory.dir("guides/${metadata.slug()}"))
            it.outputFile.set(codeDirectory.map(d -> d.file("python-output.log")))
            it.dependsOn(stageLocalPyronautArtifactsTask)
        }
    }

    private static TaskProvider<AsciidocGenerationTask> registerDocTask(Project project,
                                                                        Guide metadata,
                                                                        Directory guidesDir,
                                                                        TaskProvider<SampleProjectGenerationTask> generateTask,
                                                                        String taskSlug) {
        project.tasks.register("${taskSlug}GenerateDocs", AsciidocGenerationTask) { AsciidocGenerationTask it ->
            it.group = "guides ${metadata.slug()}"
            it.dependsOn(generateTask)
            it.description = "Generate asciidoc files for '${metadata.title()}'"
            it.slug.set(metadata.slug())
            it.inputDirectory.set(guidesDir.dir(metadata.slug()))
            it.outputDir.set(project.layout.projectDirectory.dir("src/docs/asciidoc"))
            it.metadata = metadata
        }
    }

    private static String optionName(Guide metadata, GuidesOption option) {
        "${metadata.slug()}-${option.buildTool}-${option.language}"
    }

    private static TaskProvider<Zip> registerZipTask(Project project,
                                                     Guide metadata,
                                                     GuidesOption option,
                                                     TaskProvider<SampleProjectGenerationTask> generateTask) {
        String name = optionName(metadata, option)
        String taskName = "${kebabCaseToGradleName(name)}ZipCode"
        String fromPath = "code/${metadata.slug()}/$name"
        String archiveFileName = "${name}.zip"
        project.tasks.register(taskName, Zip) { Zip it ->
            it.group = "guides ${metadata.slug()}"
            it.description = "Zips the source project for '${name}'"
            it.dependsOn(generateTask)
            it.from(project.layout.buildDirectory.dir(fromPath))
            it.archiveFileName.set(archiveFileName)
            it.destinationDirectory.set(project.layout.buildDirectory.dir("dist"))
        }
    }

    private static Map<String, Object> workflowTokens(Guide metadata,
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

        List<GuidesOption> pyronautOptions = options
                .stream()
                .filter(option -> option.buildTool == PYRONAUT)
                .collect(Collectors.toList())

        String gradleProjects = projects(metadata, gradleOptions)

        String mavenProjects = projects(metadata, mavenOptions)
        String pyronautProjects = projects(metadata, pyronautOptions)

        boolean mavenEnabled = !(CollectionUtils.isEmpty(mavenOptions) || metadata.skipMavenTests())
        boolean gradleEnabled = !(CollectionUtils.isEmpty(gradleOptions) || metadata.skipGradleTests())
        boolean pyronautEnabled = !(CollectionUtils.isEmpty(pyronautOptions) || metadata.skipPyronautTests())
        [
                gradleTask    : taskSlug + TASK_SUFFIX_GENERATE_PROJECTS,
                javaMatrix    : javaMatrix(metadata),
                slug          : metadata.slug(),
                mavenProjects : mavenProjects,
                gradleProjects: gradleProjects,
                pyronautProjects: pyronautProjects,
                paths         : workflowPaths(metadata),
                mavenEnabled  : String.valueOf(mavenEnabled),
                gradleEnabled : String.valueOf(gradleEnabled),
                pyronautEnabled: String.valueOf(pyronautEnabled)
        ] as Map
    }

    private static String projects(Guide metadata, List<GuidesOption> options) {

        List<String> combinations = options
                .stream()
                .map(option -> optionName(metadata, option))
                .collect(Collectors.toList())

        List<String> allCombinations = []

        for (String combination : combinations) {
            for (io.micronaut.guides.core.App app : metadata.apps()) {
                if (DEFAULT_APP_NAME.equals(app.name())) {
                    allCombinations << quote(combination)
                } else {
                    allCombinations << quote(combination + '/' + app.name())
                }
            }
        }

        String.join(COMMA, allCombinations)
    }

    private static TaskProvider<Copy> registerGenerateGithubActionSnapshotWorkflow(Project project,
                                                                                   Guide metadata,
                                                                                   String taskSlug) {
        Map<String, Object> tokens = workflowTokens(metadata, taskSlug)
        tokens.workflowName = "Test " + metadata.slug() + " Snapshot"
        project.tasks.register("${taskSlug}GenerateGithubActionSnapshotWorkflow", Copy) { Copy it ->
            it.from("github-action-snapshot-template.yml")
            it.into(project.layout.projectDirectory.dir(".github/workflows"))
            it.filter(ReplaceTokens, tokens: tokens)
            it.rename(new Transformer<String, String>() {
                @Override
                String transform(String s) {
                    "guide-${metadata.slug()}-snapshot.yml"
                }
            })
        }
    }

    private static TaskProvider<Copy> registerGenerateGithubActionWorkflow(Project project,
                                                                           Guide metadata,
                                                                           String taskSlug) {
        Map<String, Object> tokens = workflowTokens(metadata, taskSlug)
        tokens.workflowName = "Test " + metadata.slug()
        project.tasks.register("${taskSlug}GenerateGithubActionWorkflow", Copy) { Copy it ->
            it.from("github-action-template.yml")
            it.into(project.layout.projectDirectory.dir(".github/workflows"))
            it.filter(ReplaceTokens, tokens: tokens)
            it.rename(new Transformer<String, String>() {
                @Override
                String transform(String s) {
                    "guide-${metadata.slug()}.yml"
                }
            })
        }
    }

    private static String javaMatrix(Guide guideMetadata) {
        String.join(COMMA, JAVA_MATRIX
                .stream()
                .filter(minFilter(guideMetadata))
                .filter(maxFilter(guideMetadata))
                .map(v -> quote(v))
                .collect(Collectors.toList()))
    }

    private static Predicate<Integer> minFilter(Guide guideMetadata) {
        (guideMetadata.minimumJavaVersion() ? { int v -> v >= guideMetadata.minimumJavaVersion() } : { true }) as Predicate
    }

    private static Predicate<Integer> maxFilter(Guide guideMetadata) {
        (guideMetadata.maximumJavaVersion() ? { int v -> v <= guideMetadata.maximumJavaVersion() } : { true }) as Predicate
    }

    private static TaskProvider<SampleProjectGenerationTask> registerGenerateTask(Project project,
                                                                                  Guide metadata,
                                                                                  GuideProjectGenerator projectGenerator,
                                                                                  Directory guidesDir,
                                                                                  Provider<Directory> codeDir,
                                                                                  String taskSlug) {
        project.tasks.register("${taskSlug}${TASK_SUFFIX_GENERATE_PROJECTS}", SampleProjectGenerationTask) { SampleProjectGenerationTask it ->
            it.group = "guides ${metadata.slug()}"
            it.description = "Generate sample project for guide '${metadata.title()}'"
            it.guidesGenerator = projectGenerator
            it.slug.set(metadata.slug())
            it.inputDirectory.set(guidesDir.dir(metadata.slug()))
            if (metadata.base() != null) {
                it.baseInputDirectory.set(guidesDir.dir(metadata.base()))
            }
            it.outputDir.set(codeDir.map(s -> s.dir(metadata.slug())))
            it.guidesGenerator = projectGenerator
            it.metadata = metadata
        }
    }

    private static TaskProvider<Task> registerGuideBuild(Project project,
                                                         String taskSlug,
                                                         Guide metadata,
                                                         Collection<TaskProvider<? extends Task>> dependsOnTasks) {
        project.tasks.register("${taskSlug}${TASK_SUFFIX_BUILD}") { Task it ->
            it.group = "guides ${metadata.slug()}"
            it.dependsOn(dependsOnTasks)
            it.finalizedBy(project.tasks.named('asciidoctor'), project.tasks.named('themeGuides'))
        }
    }

    private static boolean hasPythonOption(List<GuidesOption> options) {
        options.any { GuidesOption option -> option.buildTool == PYRONAUT && option.language == PYTHON }
    }

    private static String quote(it) {
        '"' + it + '"'
    }

    private static String sys(String name) {
        System.getProperty(name)
    }

    private static boolean isGraalVMJava() {
        (
                sys("java.home") != null && Files.exists(Paths.get("${sys("java.home")}/lib/graalvm"))
        ) || [
                "jvmci.Compiler",
                "java.vendor.version",
                "java.vendor"
        ].any { sys(it)?.toLowerCase(Locale.ENGLISH)?.contains("graal") }
    }
}
