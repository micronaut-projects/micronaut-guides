package io.micronaut.guides

import io.micronaut.guides.tasks.AsciidocGenerationTask
import io.micronaut.guides.tasks.SampleProjectGenerationTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip

class GuidesPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        GuideProjectGenerator projectGenerator = new GuideProjectGenerator()

        def guidesDir = project.layout.projectDirectory.dir("guides")
        def codeDir = project.layout.buildDirectory.dir("code")

        List<GuideMetadata> metadatas = GuideProjectGenerator.parseGuidesMetadata(guidesDir.asFile, "${project.extensions.extraProperties.get("metadataConfigName")}")

        def sampleTasks = metadatas
                .findAll { Utils.process(it, false) }
                .collect { metadata ->
                    def taskSlug = metadata.slug.split("-")*.capitalize().join()
                    def options = GuideProjectGenerator.guidesOptions(metadata)

                    def generateTask = project.tasks.register("generate${taskSlug}Projects", SampleProjectGenerationTask) {
                        group = 'guides'
                        description = "Generate sample project for guide '${metadata.title}'"
                        guidesGenerator = projectGenerator
                        slug.set(metadata.slug)
                        inputDirectory.set(guidesDir.dir(metadata.slug))
                        outputDir.set(codeDir.map(s -> s.dir(metadata.slug)))
                        it.guidesGenerator = projectGenerator
                        it.metadata = metadata
                    }

                    def docTask = project.tasks.register("generate${taskSlug}Docs", AsciidocGenerationTask) {
                        group = 'guides'
                        dependsOn(generateTask)
                        description = "Generate asciidoc files for '${metadata.title}'"
                        slug.set(metadata.slug)
                        inputDirectory.set(guidesDir.dir(metadata.slug))
                        outputDir.set(project.layout.projectDirectory.dir("src/docs/asciidoc"))
                        it.metadata = metadata
                    }

                    def zippers = options.collect { option ->
                        def name = "${metadata.slug}-${option.buildTool}-${option.language}"
                        project.tasks.register("zip${name.split("-")*.capitalize().join()}Code", Zip) {
                            group = 'guides'
                            description = "Zips the source project for '${name}'"
                            dependsOn(generateTask)
                            from(project.layout.buildDirectory.dir("code/$metadata.slug/$name"))
                            archiveFileName.set("${name}.zip")
                            destinationDirectory.set(project.layout.buildDirectory.dir("dist"))
                        }
                    }

                    def zipTask = project.tasks.register("generate${taskSlug}Zips") {main ->
                        dependsOn(zippers)
                    }

                    [doc: docTask, zip: zipTask]
                }

        def sampleProjects = project.tasks.register("generateSampleProjects") {
            dependsOn(sampleTasks*.doc)
            finalizedBy(
                    project.tasks.named('generateTestScript'),
                    project.tasks.named('generateGuidesIndex'),
                    project.tasks.named('generateGuidesJsonMetadata'),
                    project.tasks.named('themeGuides')
            )
            group = 'guides'
            description = 'Generates guide applications at build/code'
        }

        project.tasks.register("generateCodeZip") {
            group = 'guides'
            description = 'Generates a ZIP file for each application at build/code into build/dist'
            dependsOn(sampleTasks*.zip, sampleProjects, 'createDist')
        }
    }
}
