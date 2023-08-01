package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootDevtools implements Feature {
    private static final Dependency DEPENDENCY_SPRING_BOOT_DEVTOOLS = Dependency.builder()
            .groupId(SpringBootDependencies.GROUP_ID_ORG_SPRINGFRAMEWORK_BOOT)
            .artifactId("spring-boot-devtools")
            .developmentOnly()
            .build();
    @Override
    public @NonNull String getName() {
        return "spring-boot-devtools";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_SPRING_BOOT_DEVTOOLS);
    }

    @Override
    public String getTitle() {
        return "Spring Boot DevTools";
    }

    @Override
    public String getDescription() {
        return "Provides fast application restarts, LiveReload, and configurations for enhanced development experience.";
    }
}
