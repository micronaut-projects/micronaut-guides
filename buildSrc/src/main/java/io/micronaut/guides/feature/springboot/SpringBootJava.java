package io.micronaut.guides.feature.springboot;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.java.Java;
import io.micronaut.starter.feature.lang.java.JavaApplicationFeature;
import jakarta.inject.Singleton;

import java.util.List;

@Replaces(Java.class)
@Singleton
public class SpringBootJava extends Java {

    @Override
    @NonNull
    public String getName() {
        return "spring-boot-java";
    }

    public SpringBootJava(List<JavaApplicationFeature> applicationFeatures) {
        super(applicationFeatures);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (SpringBootApplicationFeature.isSpringBootApplication(featureContext)) {
            processSelectedFeatures(featureContext, SpringBootApplicationFeature.class::isInstance);
        } else {
            processSelectedFeatures(featureContext, f -> !(f instanceof SpringBootApplicationFeature));
        }
    }
}
