package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.context.ApplicationContext
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.impldep.org.apache.commons.codec.language.bm.Lang

@CompileStatic
class GenerateGuidesTask  extends DefaultTask {
    @OutputDirectory
    File output

    @Input
    String guideName

    @Input
    List<String> buildTools = ['gradle', 'maven']

    @Input
    List<String> languages = ['java', 'groovy', 'kotlin']

    @Input
    List<String> features

    @Input
    String basePackage = 'example.micronaut';

    @Input
    String appName = 'complete'

    @TaskAction
    void renderGuide() {

        ApplicationContext applicationContext = ApplicationContext.run()

        GuidesGenerator guidesGenerator = applicationContext.getBean(GuidesGenerator)

        ApplicationType type = ApplicationType.DEFAULT
        String name = guideName
        String pakckageAndName = "${basePackage}.${appName}"

        GuidesOption gradleJava = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)
        GuidesOption gradleKotlin = new GuidesOption(BuildTool.GRADLE, Language.KOTLIN, TestFramework.JUNIT)
        GuidesOption gradleSpock = new GuidesOption(BuildTool.GRADLE, Language.GROOVY, TestFramework.SPOCK)
        GuidesOption mavenJava = new GuidesOption(BuildTool.MAVEN, Language.JAVA, TestFramework.JUNIT)
        GuidesOption mavenKotlin = new GuidesOption(BuildTool.MAVEN, Language.KOTLIN, TestFramework.JUNIT)
        GuidesOption mavenSpock = new GuidesOption(BuildTool.MAVEN, Language.GROOVY, TestFramework.SPOCK)
        List<GuidesOption> guidesOptionList = [gradleJava, gradleKotlin, gradleSpock, mavenJava, mavenKotlin, mavenSpock]

        for (GuidesOption guidesOption : guidesOptionList) {
            List<String> guidesFeatures = features
            if (guidesOption.language == Language.GROOVY) {
                guidesFeatures.remove('graalvm')
            }
            BuildTool buildTool = guidesOption.buildTool
            TestFramework testFramework = guidesOption.testFramework
            Language lang = guidesOption.language
            JdkVersion javaVersion = JdkVersion.JDK_8
            String folder = "${name}-${guidesOption.buildTool.toString()}-${guidesOption.language.toString()}"
            File destination = new File("${output.absolutePath}/${folder}")
            destination.mkdir()
            guidesGenerator.generateAppIntoDirectory(destination, type, pakckageAndName, features, buildTool, testFramework, lang, javaVersion)
        }

        applicationContext.close()
    }
}
