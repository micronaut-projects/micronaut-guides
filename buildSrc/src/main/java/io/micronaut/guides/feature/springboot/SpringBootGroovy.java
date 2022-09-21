package io.micronaut.guides.feature.springboot;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.groovy.Groovy;
import io.micronaut.starter.feature.lang.groovy.GroovyApplicationFeature;
import io.micronaut.starter.feature.test.Spock;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.List;

@Replaces(Groovy.class)
@Singleton
public class SpringBootGroovy extends Groovy {
    private final Dependency DEPENDENCY_GROOVY = Dependency.builder()
            .groupId("org.codehaus.groovy")
            .artifactId("groovy")
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return "spring-boot-groovy";
    }

    protected List<GroovyApplicationFeature> applicationFeatures;

    public SpringBootGroovy(List<GroovyApplicationFeature> applicationFeatures, Spock spock) {
        super(applicationFeatures, spock);
        this.applicationFeatures = applicationFeatures;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(SpringBootAppFeature.class)) {
            processSelectedFeaturesAsSpringBootApp(featureContext);
        } else {
            processSelectedFeaturesAsMicronautApp(featureContext);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            generatorContext.addDependency(DEPENDENCY_GROOVY);
        } else {
            if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
                generatorContext.getBuildProperties().put("groovyVersion", VersionInfo.getDependencyVersion("groovy").getValue());
            }
            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.micronaut.groovy")
                    .artifactId("micronaut-runtime-groovy")
                    .compile());
        }
    }

    private void processSelectedFeaturesAsSpringBootApp(FeatureContext featureContext) {
        applicationFeatures.stream()
                .filter(SpringBootApplicationFeature.class::isInstance)
                .filter(f -> f.supports(featureContext.getApplicationType()))
                .findFirst()
                .ifPresent(feature -> processSelectedFeatures(featureContext, feature));

    }
    private void processSelectedFeaturesAsMicronautApp(FeatureContext featureContext) {
        applicationFeatures.stream()
                .filter(f -> !(f instanceof SpringBootApplicationFeature))
                .filter(f -> f.supports(featureContext.getApplicationType()))
                .findFirst()
                .ifPresent(feature -> processSelectedFeatures(featureContext, feature));
    }

    private void processSelectedFeatures(FeatureContext featureContext, GroovyApplicationFeature feature) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            featureContext.addFeature(feature);
        }
    }
}
