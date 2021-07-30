package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.template.gitCommitIdPlugin;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerWritable;

import javax.inject.Singleton;

@Singleton
public class GitProperties implements Feature {

    private final PomDependencyVersionResolver resolver;

    public GitProperties(PomDependencyVersionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "git-properties";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.gorylenko.gradle-git-properties")
                    .lookupArtifactId("gradle-git-properties")
                    .build());
            return;
        }

        String artifactId = "git-commit-id-plugin";

        BuildProperties props = generatorContext.getBuildProperties();
        resolver.resolve(artifactId)
                .ifPresent(coordinate -> props.put("gitCommitIdPlugin.version", coordinate.getVersion()));

        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .artifactId(artifactId)
                .extension(new RockerWritable(gitCommitIdPlugin.template()))
                .build());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }
}
