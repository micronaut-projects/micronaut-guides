package io.micronaut.guides.feature;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.database.MariaDB;
import io.micronaut.starter.feature.database.MySQL;
import io.micronaut.starter.feature.database.Oracle;
import io.micronaut.starter.feature.database.SQLServer;
import io.micronaut.starter.feature.migration.Flyway;
import io.micronaut.starter.feature.oraclecloud.OracleCloudAutonomousDatabase;
import jakarta.inject.Singleton;

@Replaces(Flyway.class)
@Singleton
public class FlywayReplacement extends Flyway {

    @Override
    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.flywayDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_FLYWAY)
                .compile());
        if (generatorContext.isFeaturePresent(MySQL.class) || generatorContext.isFeaturePresent(MariaDB.class)) {
            generatorContext.addDependency(DEPENDENCY_FLYWAY_MYSQL);
        }
        if (generatorContext.isFeaturePresent(SQLServer.class)) {
            generatorContext.addDependency(DEPENDENCY_FLYWAY_SQLSERVER);
        }
        if (generatorContext.isFeaturePresent(Oracle.class) || generatorContext.isFeaturePresent(OracleCloudAutonomousDatabase.class)) {
            generatorContext.addDependency(DEPENDENCY_FLYWAY_ORACLE);
        }
    }
}
