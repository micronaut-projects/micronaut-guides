package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.database.MariaDB;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

@Singleton
public class TestResources implements Feature {

    public static final String PROPERTY_DATASOURCES_DEFAULT_DB_TYPE = "datasources.default.db-type";

    @Override
    @NonNull
    public String getName() {
        return "test-resources";
    }

    @Override
    @NonNull
    public String getTitle() {
        return "Micronaut Test Resources";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for managing external resources which are required during development or testing.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(MicronautTestResourcesGradlePlugin.builder().build());
        } else if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            //TODO
        }
        if (generatorContext.getFeatures().hasFeature(MariaDB.class)) {
            generatorContext.getConfiguration().put(PROPERTY_DATASOURCES_DEFAULT_DB_TYPE, DbType.MariaDB.toString());
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-test-resources/snapshot/guide/";
    }
}

