package io.micronaut.guides

import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import org.gradle.api.GradleException
import org.openapitools.codegen.ClientOptInput
import org.openapitools.codegen.DefaultGenerator
import org.openapitools.codegen.Generator
import org.openapitools.codegen.GeneratorNotFoundException
import org.openapitools.codegen.config.CodegenConfigurator

import static io.micronaut.starter.api.TestFramework.JUNIT
import static io.micronaut.starter.api.TestFramework.SPOCK
import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.MAVEN

@Singleton
class OpenAPIGenerator {
    static void generate(
            File inputDir, File destination, Language lang, String destinationPackage, GuideMetadata.OpenAPIGeneratorConfig config,
            TestFramework testFramework, BuildTool buildTool
    ) {
        if (!config.definitionFile) {
            throw new GradleException("Need to specify the 'definitionFile' property if using 'openAPIGeneratorConfig'")
        }
        if (!config.generatorName) {
            throw new GradleException("Need to specify the 'generatorName' property if using 'openAPIGeneratorConfig'")
        }

        File definitionFilePath = new File(new File(inputDir, lang.getExtension()), config.definitionFile)
        if (!definitionFilePath.isFile()) {
            throw new GradleException("OpenAPI definition file '" + definitionFilePath.getPath() + "' does not exist")
        }
        CodegenConfigurator configurator = new CodegenConfigurator();
        configurator.setInputSpec(definitionFilePath.getPath());
        configurator.setGeneratorName(config.generatorName)
        configurator.setOutputDir(destination.getPath())

        if (testFramework == SPOCK) {
            configurator.addAdditionalProperty("test", "spock")
        } else if (testFramework == JUNIT) {
            configurator.addAdditionalProperty("test", "junit")
        }
        if (buildTool == GRADLE) {
            configurator.addAdditionalProperty("build", "gradle")
        } else if (buildTool == MAVEN) {
            configurator.addAdditionalProperty("build", "maven")
        }
        configurator.addAdditionalProperty("controllerPackage", destinationPackage + ".controller")
        configurator.addAdditionalProperty("apiPackage", destinationPackage + ".api")
        configurator.addAdditionalProperty("modelPackage", destinationPackage + ".model")

        config.properties?.each {
            configurator.addAdditionalProperty(it.key, it.value);
        }

        try {
            ClientOptInput clientOptInput = configurator.toClientOptInput()
            Generator generator = new DefaultGenerator()
            generator.opts(clientOptInput)
            generator.generate()
        } catch (GeneratorNotFoundException e) {
            throw new GradleException("OpenAPI couldn't find specified generator: \"" +
                    e.message + "\". Check 'generatorName' property. ")
        } catch (Exception e) {
            throw new GradleException("OpenAPI generator failed with \"" + e.message + "\"");
        }
    }
}
