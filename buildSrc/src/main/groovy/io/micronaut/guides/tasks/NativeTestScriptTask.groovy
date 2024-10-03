package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import io.micronaut.guides.core.GuideMetadata
import io.micronaut.guides.TestScriptGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CompileStatic
@CacheableTask
abstract class NativeTestScriptTask extends DefaultTask {

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
        TestScriptGenerator.generateNativeTestScript(scriptFile.get().asFile.parentFile, [metadata], false)
    }
}
