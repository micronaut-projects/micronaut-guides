package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class ApacheGroovy implements Feature {
    private static final Dependency DEPENDENCY_APACHE_GROOVY = Dependency.builder().groupId("org.apache.groovy").artifactId("groovy").scope(Scope.COMPILE).build();
    @Override
    public @NonNull String getName() {
        return "apache-groovy";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the apache groovy dependency.";
    }

    @Override
    public String getCategory() {
        return Category.LANGUAGES;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_APACHE_GROOVY);
    }
}
