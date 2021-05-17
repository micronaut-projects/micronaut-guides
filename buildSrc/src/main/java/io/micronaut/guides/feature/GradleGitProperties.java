package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import javax.inject.Singleton;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;

@Singleton
public class GradleGitProperties implements Feature  {

    @Override
    @NonNull
    public String getName() {
        return "gradle-git-properties";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.gorylenko.gradle-git-properties")
                    .lookupArtifactId("gradle-git-properties")
                    .build());
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }
}
