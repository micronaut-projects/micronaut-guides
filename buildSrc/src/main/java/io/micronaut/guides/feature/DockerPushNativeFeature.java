package io.micronaut.guides.feature;

import io.micronaut.guides.feature.template.dockerPushNative;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerWritable;

public abstract class DockerPushNativeFeature implements Feature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin
                    .builder()
                    .id("idea") // ID is considered required. Adding a not harmful plugin
                    .extension(new RockerWritable(dockerPushNative.template(getImage())))
                    .build());
        }
    }

    protected abstract String getImage();

    @Override
    public boolean isPreview() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
