package io.micronaut.guides.feature;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class ConfigurationValidationFeature implements Feature {
    private final PomDependencyVersionResolver resolver;

    public ConfigurationValidationFeature(PomDependencyVersionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public String getName() {
        return "configuration-validation";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("io.micronaut.configuration.validation")
                    .lookupArtifactId("micronaut-gradle-plugin")
                    .version(resolver.resolve("micronaut-gradle-plugin").get().getVersion())
                    .order(100)
                    .build());
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
