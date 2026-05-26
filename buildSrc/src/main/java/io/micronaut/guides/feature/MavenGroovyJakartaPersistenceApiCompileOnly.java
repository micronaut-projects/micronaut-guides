package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.COMPILE_ONLY;

@Singleton
public class MavenGroovyJakartaPersistenceApiCompileOnly extends AbstractFeature {

    public MavenGroovyJakartaPersistenceApiCompileOnly() {
        super("maven-groovy-jakarta-persistence-api-compile-only", "jakarta.persistence-api", COMPILE_ONLY);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getLanguage() == Language.GROOVY) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("jakarta.persistence")
                    .artifactId("jakarta.persistence-api")
                    .scope(COMPILE_ONLY));
        }
    }
}
