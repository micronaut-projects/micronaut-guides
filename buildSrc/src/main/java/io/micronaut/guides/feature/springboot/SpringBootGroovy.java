package io.micronaut.guides.feature.springboot;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.groovy.Groovy;
import io.micronaut.starter.feature.lang.groovy.GroovyApplicationFeature;
import io.micronaut.starter.feature.test.Spock;
import jakarta.inject.Singleton;

import java.util.List;

import static io.micronaut.guides.feature.springboot.SpringBootApplicationFeature.isSpringBootApplication;

@Replaces(Groovy.class)
@Singleton
public class SpringBootGroovy extends Groovy {
    @Override
    @NonNull
    public String getName() {
        return "spring-boot-groovy";
    }

    public SpringBootGroovy(List<GroovyApplicationFeature> applicationFeatures, Spock spock) {
        super(applicationFeatures, spock);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (isSpringBootApplication(generatorContext)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.codehaus.groovy")
                    .artifactId("groovy")
                    .compile());
        } else {
            super.apply(generatorContext);
        }
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.isPresent(SpringBootAppFeature.class)) {
            processSelectedFeatured(featureContext, SpringBootApplicationFeature.class::isInstance);
        } else {
            processSelectedFeatured(featureContext, f -> !(f instanceof SpringBootApplicationFeature));
        }
    }
}
