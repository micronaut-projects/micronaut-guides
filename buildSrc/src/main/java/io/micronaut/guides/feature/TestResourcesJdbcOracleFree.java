package io.micronaut.guides.feature;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Singleton
public class TestResourcesJdbcOracleFree implements Feature, TestResourcesAdditionalModulesProvider {

    private static final String NAME = "test-resources-jdbc-oracle-free";
    private static final MavenCoordinate DEPENDENCY = new MavenCoordinate(
            "io.micronaut.testresources",
            "micronaut-test-resources-jdbc-oracle-free",
            null);

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
    }

    @Override
    public List<String> getTestResourcesAdditionalModules(GeneratorContext generatorContext) {
        return Collections.emptyList();
    }

    @Override
    public List<MavenCoordinate> getTestResourcesDependencies(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            return List.of(DEPENDENCY);
        }
        return Collections.emptyList();
    }
}
