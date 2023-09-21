package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import org.apache.tools.ant.util.TeeOutputStream
import org.gradle.api.file.RegularFileProperty
import org.gradle.process.ExecOperations
import org.gradle.process.ExecResult
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters

import javax.inject.Inject

@CompileStatic
abstract class NativeTestScriptRunnerWorkAction implements WorkAction<NativeTestScriptRunnerWorkParameters> {

    @Inject
    abstract ExecOperations getExecOperations()

    @Override
    void execute() {
        File workDir = parameters.nativeTestScript.get().asFile.parentFile
        try (OutputStream file = new FileOutputStream(parameters.outputFile.get().asFile)) {
            ExecResult result = execOperations.exec(execSpec -> {
                OutputStream oldOut = execSpec.standardOutput
                OutputStream oldErr = execSpec.errorOutput
                execSpec
                        .commandLine("./native-test.sh")
                        .setStandardOutput(new TeeOutputStream(oldOut, file))
                        .setErrorOutput(new TeeOutputStream(oldErr, file))
                        .workingDir(workDir)
            })
            result.assertNormalExitValue()
        }
    }

    static interface NativeTestScriptRunnerWorkParameters extends WorkParameters {
        RegularFileProperty getOutputFile()
        RegularFileProperty getNativeTestScript()
    }
}
