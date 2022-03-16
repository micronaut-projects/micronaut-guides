package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import io.micronaut.guides.GuideMetadata
import io.micronaut.guides.TestScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

@CompileStatic
@CacheableTask
abstract class TestScriptTask extends DefaultTask {

    @Internal
    GuideMetadata metadata

    @OutputFile
    abstract RegularFileProperty getScriptFile()

    @TaskAction
    def perform() {
        TestScriptGenerator.generateTestScript(scriptFile.get().asFile.parentFile, Collections.singletonList(metadata), false)
    }
}
