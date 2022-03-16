package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
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
        File workDir = parameters.testScript.get().asFile.parentFile
        ExecResult result = execOperations.exec(execSpec -> execSpec
                .commandLine("./test.sh")
                .workingDir(workDir)
        )
        result.assertNormalExitValue()
    }

    static interface TestScriptRunnerWorkParameters extends WorkParameters {
        RegularFileProperty getTestScript()
    }
}
