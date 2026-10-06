package io.micronaut.guides.tasks

import groovy.transform.CompileStatic
import io.micronaut.guides.GuideProjectGenerator
import io.micronaut.guides.core.Guide
import io.micronaut.guides.core.GuidesOption
import io.micronaut.starter.options.Language
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.TaskAction

import static org.gradle.api.tasks.PathSensitivity.RELATIVE

@CompileStatic
@CacheableTask
abstract class SampleProjectGenerationTask extends DefaultTask {

    static List<String> outputDirectoryNames(Guide metadata, Language language) {
        GuideProjectGenerator.guidesOptions(metadata)
                .findAll { GuidesOption option -> language == null || option.language == language }
                .collect { GuidesOption option -> GuideProjectGenerator.folderName(metadata.slug(), option) }
    }

    @Internal
    GuideProjectGenerator guidesGenerator

    @Internal
    Guide metadata

    @Input
    abstract Property<String> getSlug()

    @Optional
    @Input
    abstract Property<String> getLanguage()

    @InputDirectory
    @PathSensitive(RELATIVE)
    abstract DirectoryProperty getInputDirectory()

    @Optional
    @InputDirectory
    @PathSensitive(RELATIVE)
    abstract DirectoryProperty getBaseInputDirectory()

    @Internal
    abstract DirectoryProperty getOutputDir()

    @OutputDirectories
    abstract ConfigurableFileCollection getOutputDirectories()

    @TaskAction
    def perform() {
        File outputDirectory = outputDir.get().asFile
        Language languageFilter = language.isPresent() ? Language.valueOf(language.get()) : null
        if (languageFilter == null) {
            project.delete(outputDirectory)
        } else {
            project.delete(outputDirectories)
        }
        guidesGenerator.generateOne(metadata, inputDirectory.get().asFile, outputDirectory, languageFilter)
    }
}
