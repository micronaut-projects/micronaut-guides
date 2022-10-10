package io.micronaut.guides.feature.springboot;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

@Singleton
public class SpringBootStarterWeb implements SpringBootStarter {
    @Override
    @NonNull
    public String getName() {
        return "spring-boot-starter-web";
    }

    @Override
    public String getTitle() {
        return "Spring Web";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Build web, including RESTful, applications using Spring MVC. Uses Apache Tomcat as the default embedded container.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(springBootDependency("spring-boot-starter-web").compile());
        generatorContext.addDependency(springBootDependency("spring-boot-starter-test").test());
    }

    private static Dependency.Builder springBootDependency(String artifactId) {
        return Dependency.builder().groupId("org.springframework.boot").artifactId(artifactId);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return ApplicationType.DEFAULT == applicationType;
    }
}
