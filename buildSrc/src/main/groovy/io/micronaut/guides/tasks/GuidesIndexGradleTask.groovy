package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import io.micronaut.guides.GuideMetadata
import io.micronaut.guides.IndexGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.TaskAction

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CompileStatic
@CacheableTask
abstract class GuidesIndexGradleTask extends DefaultTask {

    @Internal
    GuideMetadata metadata

    @InputFile
    @PathSensitive(RELATIVE)
    abstract RegularFileProperty getTemplate()

    @OutputDirectory
    abstract DirectoryProperty getOutputDir()

    @TaskAction
    def perform() {
        IndexGenerator.generateGuidesIndex(template.get().asFile, outputDir.get().asFile, Collections.singletonList(metadata))
    }
}
