package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.RequiresMavenLocal;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

@Singleton
public class SourcegenJava implements RequiresMavenLocal {
    public static final String ARTIFACT_ID_SOURCEGEN_ANNOTATIONS = "micronaut-sourcegen-annotations";
    public static final String ARTIFACT_ID_SOURCEGEN_GENERATOR_JAVA = "micronaut-sourcegen-generator-java";
    public static final String ARTIFACT_ID_SOURCEGEN_GENERATOR_KOTLIN = "micronaut-sourcegen-generator-kotlin";

    @Override
    public @NonNull String getName() {
        return "sourcegen-generator";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId(ARTIFACT_ID_SOURCEGEN_ANNOTATIONS)
                .scope(Scope.COMPILE));
        if (generatorContext.getLanguage() == Language.JAVA) {
            generatorContext.addDependency(Dependency.builder()
                    .lookupArtifactId(ARTIFACT_ID_SOURCEGEN_GENERATOR_JAVA)
                    .scope(Scope.ANNOTATION_PROCESSOR));
        } else if (generatorContext.getLanguage() == Language.KOTLIN) {
            generatorContext.addDependency(Dependency.builder()
                    .lookupArtifactId(ARTIFACT_ID_SOURCEGEN_GENERATOR_KOTLIN)
                    .scope(Scope.ANNOTATION_PROCESSOR));
        }
    }
}

