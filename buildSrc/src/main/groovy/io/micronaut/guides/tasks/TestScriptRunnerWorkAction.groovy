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
abstract class TestScriptRunnerWorkAction implements WorkAction<TestScriptRunnerWorkParameters> {

    @Inject
    abstract ExecOperations getExecOperations()

    @Override
    void execute() {
//        File workDir = parameters.testScript.get().asFile.parentFile
//        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win")
//        try (OutputStream file = new FileOutputStream(parameters.outputFile.get().asFile)) {
//            ExecResult result = execOperations.exec(execSpec -> {
//                OutputStream oldOut = execSpec.standardOutput
//                OutputStream oldErr = execSpec.errorOutput
//                String command = "./test.sh"
//                if (isWindows) {
//                    // Git bash must be on the path
//                    command = Arrays.asList("C:\\Program Files\\Git\\bin\\bash.exe", "-c", "./test.sh");
//                }
//                execSpec
//                        .commandLine(command)
//                        .setStandardOutput(new TeeOutputStream(oldOut, file))
//                        .setErrorOutput(new TeeOutputStream(oldErr, file))
//                        .workingDir(workDir)
//            })
//            result.assertNormalExitValue()
//        }
    }

    static interface TestScriptRunnerWorkParameters extends WorkParameters {
        RegularFileProperty getOutputFile()
        RegularFileProperty getTestScript()
    }
}
