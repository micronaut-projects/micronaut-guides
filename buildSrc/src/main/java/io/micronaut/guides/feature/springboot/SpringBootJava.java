package io.micronaut.guides.feature.springboot;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.feature.ApplicationFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.java.JavaApplicationFeature;
import jakarta.inject.Singleton;
import io.micronaut.starter.feature.lang.java.Java;

import java.util.List;

@Replaces(Java.class)
@Singleton
public class SpringBootJava extends Java {
    @Override
    @NonNull
    public String getName() {
        return "spring-boot-java";
    }

    private final List<JavaApplicationFeature> applicationFeatures;

    public SpringBootJava(List<JavaApplicationFeature> applicationFeatures) {
        super(applicationFeatures);
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

    private void processSelectedFeatures(FeatureContext featureContext, JavaApplicationFeature feature) {
        if (!featureContext.isPresent(ApplicationFeature.class)) {
            featureContext.addFeature(feature);
        }
    }
}
