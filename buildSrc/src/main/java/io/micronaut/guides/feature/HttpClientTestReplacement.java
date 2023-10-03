package io.micronaut.guides.feature;


import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.awslambdacustomruntime.AwsLambdaCustomRuntime;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.httpclient.HttpClientFeature;
import io.micronaut.starter.feature.validator.MicronautHttpValidation;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;
import java.util.Set;
import io.micronaut.starter.feature.other.HttpClientTest;
import io.micronaut.starter.feature.other.HttpClient;

import static io.micronaut.starter.feature.other.HttpClient.ARTIFACT_ID_MICRONAUT_HTTP_CLIENT;

@Replaces(HttpClientTest.class)
@Singleton
public class HttpClientTestReplacement implements DefaultFeature {

    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_TEST = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT)
            .test()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_CLIENT_COMPILE_ONLY = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_HTTP_CLIENT)
            .compileOnly()
            .build();

    @Override
    public String getName() {
        return "http-client-test";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(HttpClientFeature.class::isInstance);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().hasFeature(AwsLambdaCustomRuntime.class) || (generatorContext.getFeatures().hasFeature(AwsLambda.class) && generatorContext.getFeatures().hasFeature(GraalVM.class))) {
            generatorContext.addDependency(HttpClient.DEPENDENCY_MICRONAUT_HTTP_CLIENT);
        } else if (generatorContext.getApplicationType() == ApplicationType.DEFAULT) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_HTTP_CLIENT_TEST);
            if (generatorContext.hasFeature(MicronautHttpValidation.class) && generatorContext.getBuildTool().isGradle()) {
                generatorContext.addDependency(DEPENDENCY_MICRONAUT_HTTP_CLIENT_COMPILE_ONLY);
            }
        }
    }
}