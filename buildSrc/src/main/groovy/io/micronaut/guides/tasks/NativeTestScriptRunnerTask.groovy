package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.workers.WorkQueue
import org.gradle.workers.WorkerExecutor

import javax.inject.Inject

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CacheableTask
@CompileStatic
abstract class NativeTestScriptRunnerTask extends DefaultTask {

    @InputFile
    @PathSensitive(RELATIVE)
    abstract RegularFileProperty getNativeTestScript()

    @InputDirectory
    @PathSensitive(RELATIVE)
    abstract DirectoryProperty getGuideSourceDirectory()

    @OutputFile
    abstract RegularFileProperty getOutputFile()

    @Inject
    abstract WorkerExecutor getWorkerExecutor()

    @TaskAction
    void runScript() {
        WorkQueue queue = workerExecutor.noIsolation()

        queue.submit(NativeTestScriptRunnerWorkAction) { parameters ->
            parameters.nativeTestScript.set(nativeTestScript)
            parameters.outputFile.set(outputFile)
        }
        queue.await()
    }
}
