package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.template.graalpyMavenPluginPackage;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import static io.micronaut.starter.application.ApplicationType.DEFAULT;

@Singleton
public class GraalPyMavenPluginPackage implements Feature {

    private final PomDependencyVersionResolver resolver;

    public GraalPyMavenPluginPackage(PomDependencyVersionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "graalpy-maven-plugin-package";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .groupId("org.graalvm.python")
                .artifactId("graalpy-maven-plugin")
                .extension(new RockerWritable(graalpyMavenPluginPackage.template()))
                .build());
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == DEFAULT;
    }
}
