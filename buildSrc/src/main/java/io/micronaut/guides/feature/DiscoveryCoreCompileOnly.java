package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.COMPILE_ONLY;

@Singleton
public class DiscoveryCoreCompileOnly extends AbstractFeature {

    public DiscoveryCoreCompileOnly() {
        super("discovery-core-compile-only", "micronaut-discovery-core", COMPILE_ONLY);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getLanguage() == Language.GROOVY) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.micronaut")
                    .artifactId("micronaut-discovery-core")
                    .scope(COMPILE_ONLY));
        }
    }
}
