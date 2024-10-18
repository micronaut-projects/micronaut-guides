package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import io.micronaut.guides.GuideMetadata
import io.micronaut.guides.TestScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.TaskAction

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CompileStatic
@CacheableTask
abstract class TestScriptTask extends DefaultTask {

    @Input
    abstract Property<String> getGuideSlug()

    @InputFile
    @PathSensitive(RELATIVE)
    abstract RegularFileProperty getMetadataFile()

    @Internal
    GuideMetadata metadata

    @OutputFile
    abstract RegularFileProperty getScriptFile()

    @TaskAction
    def perform() {
        TestScriptGenerator.generateTestScript(scriptFile.get().asFile.parentFile, [metadata], false)
    }
}
