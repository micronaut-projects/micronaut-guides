package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.core.util.StringUtils
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@CompileStatic
class GenerateGuidesTask  extends DefaultTask {
    @OutputDirectory
    File output

    @Input
    String guideName

    @Input
    String micronaut

    @Input
    String guideTitle

    @Input
    String guideIntro

    @Input
    List<String> authors

    @Input
    String asciidocFileName

    @Input
    List<String> buildTools = ['gradle', 'maven']

    @Input
    List<String> languages = ['java', 'groovy', 'kotlin']

    @Optional
    @Input
    String testFramework

    @Input
    List<String> features

    @Input
    String basePackage = 'example.micronaut';

    @Input
    String appName = 'complete'


    @TaskAction
    void renderGuide() {
        /*
        ApplicationContext applicationContext = ApplicationContext.run()

        GuidesGenerator guidesGenerator = applicationContext.getBean(GuidesGenerator)

        ApplicationType type = ApplicationType.DEFAULT
        String name = guideName
        String pakckageAndName = "${basePackage}.${appName}"




        applicationContext.close()

         */
    }
}
