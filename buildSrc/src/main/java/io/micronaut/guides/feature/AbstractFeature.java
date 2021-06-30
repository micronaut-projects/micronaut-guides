package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Feature;

import static io.micronaut.starter.build.dependencies.Scope.COMPILE;

public abstract class AbstractFeature implements Feature {

    private final String name;
    private final String artifactId;
    private final Scope scope;

    protected AbstractFeature(String artifactId) {
        this(artifactId, artifactId, COMPILE);
    }

    protected AbstractFeature(String name,
                              String artifactId) {
        this(name, artifactId, COMPILE);
    }

    protected AbstractFeature(String artifactId,
                              Scope scope) {
        this(artifactId, artifactId, scope);
    }

    protected AbstractFeature(String name,
                              String artifactId,
                              Scope scope) {
        this.name = name;
        this.artifactId = artifactId;
        this.scope = scope;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependency(generatorContext, artifactId, scope);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    protected void addDependency(GeneratorContext generatorContext,
                                 String artifactId) {
        addDependency(generatorContext, artifactId, COMPILE);
    }

    protected void addDependency(GeneratorContext generatorContext,
                                 String artifactId,
                                 Scope scope) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(artifactId)
                .scope(scope));
    }

    protected void addDependencyWithoutLookup(GeneratorContext generatorContext,
                                              String groupId) {
        generatorContext.addDependency(Dependency.builder()
                .groupId(groupId)
                .artifactId(artifactId)
                .scope(scope));
    }
}
