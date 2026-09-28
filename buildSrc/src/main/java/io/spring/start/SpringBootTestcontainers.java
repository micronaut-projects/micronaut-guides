package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootTestcontainers implements Feature {

    public static final Dependency DEPENDENCY_SPRINGBOOT_TESTCONTAINERS = Dependency.builder()
            .groupId(SpringBootDependencies.GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId("spring-boot-testcontainers")
            .test()
            .build();

    private final TestContainersJunitJupiter testContainersJunitJupiter;

    public SpringBootTestcontainers(TestContainersJunitJupiter testContainersJunitJupiter) {
        this.testContainersJunitJupiter = testContainersJunitJupiter;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(TestContainersJunitJupiter.class, testContainersJunitJupiter);
    }

    @Override
    public @NonNull String getName() {
        return "spring-boot-testcontainers";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_SPRINGBOOT_TESTCONTAINERS);
    }
}
