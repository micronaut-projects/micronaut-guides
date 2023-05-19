package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import io.micronaut.guides.GuideMetadata
import io.micronaut.guides.GuideProjectGenerator
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.TaskAction

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CompileStatic
@CacheableTask
abstract class SampleProjectGenerationTask extends DefaultTask {

    @Internal
    GuideProjectGenerator guidesGenerator

    @Internal
    GuideMetadata metadata

    @Input
    abstract Property<String> getSlug()

    @InputDirectory
    @PathSensitive(RELATIVE)
    abstract DirectoryProperty getInputDirectory()

    @Optional
    @InputDirectory
    @PathSensitive(RELATIVE)
    abstract DirectoryProperty getBaseInputDirectory()

    @OutputDirectory
    abstract DirectoryProperty getOutputDir()

    @TaskAction
    def perform() {
        guidesGenerator.generateOne(metadata, inputDirectory.get().asFile, outputDir.get().asFile)
    }
}
