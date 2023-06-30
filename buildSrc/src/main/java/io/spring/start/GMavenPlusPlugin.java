package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Feature;
import io.spring.start.templates.gmaven;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class GMavenPlusPlugin implements Feature {
    @Override
    public @NonNull String getName() {
        return "gmaven-plus-plugin";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .groupId("org.codehaus.gmavenplus")
                .artifactId("gmavenplus-plugin")
                .extension(new RockerWritable(gmaven.template()))
                .build());
    }
}
