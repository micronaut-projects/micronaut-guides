package io.micronaut.guides

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
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

@CompileStatic
class OpenAPIGenerator {

    public static final String TEST = "test"
    public static final String PROPERTY_BUILD = "build"
    public static final String PROPERTY_CONTROLLER_PACKAGE = "controllerPackage"
    public static final String PROPERTY_API_PACKAGE = "apiPackage"
    public static final String PROPERTY_MODEL_PACKAGE = "modelPackage"
    public static final String PACKAGE_CONTROLLER = ".controller"
    public static final String PACKAGE_API = ".api"
    public static final String PACKAGE_MODEL = ".model"

    static void generate(File inputDir,
                         File destination,
                         Language lang,
                         String destinationPackage,
                         GuideMetadata.OpenAPIGeneratorConfig config,
                         TestFramework testFramework,
                         BuildTool buildTool) {
        if (!config.definitionFile) {
            throw new GradleException("Need to specify the 'definitionFile' property if using 'openAPIGeneratorConfig'")
        }
        File definitionFilePath = new File(new File(inputDir, lang.getExtension()), config.definitionFile)
        if (!definitionFilePath.isFile()) {
            throw new GradleException("OpenAPI definition file '" + definitionFilePath.getPath() + "' does not exist")
        }
        CodegenConfigurator configurator = createConfiguration(definitionFilePath, config, destination, testFramework, buildTool, destinationPackage)

        try {
            ClientOptInput clientOptInput = configurator.toClientOptInput()
            Generator generator = new DefaultGenerator()
            generator.opts(clientOptInput)
            generator.generate()
        } catch (GeneratorNotFoundException e) {
            throw new GradleException("OpenAPI couldn't find specified generator: \"" +
                    e.message + "\". Check 'generatorName' property. ")
        } catch(NoSuchMethodError e) {
            throw new GradleException("OpenAPI generator failed with \"" + e.message + "\"")
        } catch (Exception e) {
            throw new GradleException("OpenAPI generator failed with \"" + e.message + "\"")
        }
    }

    private static CodegenConfigurator createConfiguration(File definitionFilePath,
                                                           GuideMetadata.OpenAPIGeneratorConfig config,
                                                           File destination,
                                                           TestFramework testFramework,
                                                           BuildTool buildTool,
                                                           String destinationPackage) {
        CodegenConfigurator configurator = new CodegenConfigurator()
        configurator.setInputSpec(definitionFilePath.getPath())
        configurator.setGeneratorName(config.generatorName)
        configurator.setOutputDir(destination.getPath())
        config.globalProperties?.each { k, v -> configurator.addGlobalProperty(k, v) }
        configurationAdditionalProperties(config, testFramework, buildTool, destinationPackage).each { k, v ->
            configurator.addAdditionalProperty(k, v)
        }
        configurator
    }

    @NonNull
    private static Map<String, Object> configurationAdditionalProperties(@NonNull GuideMetadata.OpenAPIGeneratorConfig config,
                                                                         @NonNull TestFramework testFramework,
                                                                         @NonNull BuildTool buildTool,
                                                                         @NonNull String destinationPackage) {
        Map<String, Object> additionalProperties = [:]
        testProperty(testFramework).ifPresent(value -> additionalProperties.put(TEST, value))
        buildProperty(buildTool).ifPresent(value -> additionalProperties.put(PROPERTY_BUILD, value))
        additionalProperties.put(PROPERTY_CONTROLLER_PACKAGE, destinationPackage + PACKAGE_CONTROLLER)
        additionalProperties.put(PROPERTY_API_PACKAGE, destinationPackage + PACKAGE_API)
        additionalProperties.put(PROPERTY_MODEL_PACKAGE, destinationPackage + PACKAGE_MODEL)
        config.properties?.each {k, v ->
            additionalProperties.put(k, v)
        }
        additionalProperties
    }

    @NonNull
    private static Optional<String> buildProperty(@NonNull BuildTool buildTool) {
        if (buildTool.isGradle()) {
            return Optional.of(GRADLE.toString())
        } else if (buildTool == MAVEN) {
            return Optional.of(MAVEN.toString())
        }
        Optional.empty()
    }

    @NonNull
    private static Optional<String> testProperty(@NonNull TestFramework testFramework) {
        if (testFramework == SPOCK || testFramework == JUNIT) {
            return Optional.of(testFramework.toString())
        }
        Optional.empty()
    }
}
