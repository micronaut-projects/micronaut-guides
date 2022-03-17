package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

@CacheableTask
@CompileStatic
abstract class TestScriptRunnerTask extends DefaultTask {

    @InputFile
    @PathSensitive(PathSensitivity.RELATIVE)
    abstract RegularFileProperty getTestScript()

    @InputDirectory
    @PathSensitive(PathSensitivity.RELATIVE)
    abstract DirectoryProperty getGuideSourceDirectory()

    @OutputFile
    abstract RegularFileProperty getOutputFile()

    @Inject
    abstract WorkerExecutor getWorkerExecutor();

    // We have to wait for the first task to complete as mvnw corrupts the wrapper if downloaded in parallel
    @Internal
    abstract Property<Boolean> getAwait()

    @TaskAction
    void runScript() {
        WorkQueue queue = workerExecutor.noIsolation()

        queue.submit(TestScriptRunnerWorkAction) { parameters ->
            parameters.testScript.set(testScript)
            parameters.outputFile.set(outputFile)
        }

        // Just run one at a time for now, when we try to parallelize we will need to await if
        // getAwait() is true, so that the maven wrapper is downloaded uncorrupted.
        queue.await()
    }
}
