package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.database.MariaDB;
import io.micronaut.starter.feature.database.MySQL;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbc;

import io.micronaut.starter.feature.migration.Flyway;
import io.micronaut.starter.feature.migration.MigrationFeature;
import jakarta.inject.Singleton;

@Singleton
public class MySQLTestResources extends DatabaseDriverFeatureTestResources {

    public MySQLTestResources(JdbcFeature jdbcFeature, TestResources testResources) {
        super(jdbcFeature, testResources);
    }

    @Override
    @NonNull
    public String getName() {
        return "mysql-testresources";
    }

    @Override
    public String getTitle() {
        return "MySQL";
    }

    @Override
    public String getDescription() {
        return "Adds the MySQL driver and default config";
    }

    @Override
    public String getR2dbcUrl() {
        return "r2dbc:mysql://localhost:3306/db";
    }

    @Override
    public String getDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String getDataDialect() {
        return "MYSQL";
    }

    @Override
    public boolean embedded() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.isFeaturePresent(R2dbc.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("dev.miku")
                    .artifactId("r2dbc-mysql")
                    .runtime());
            if (!generatorContext.isFeaturePresent(MigrationFeature.class)) {
                return;
            }
        }

        generatorContext.addDependency(Dependency.builder()
                .groupId("mysql")
                .artifactId("mysql-connector-java")
                .runtime());

        if (generatorContext.isFeaturePresent(Flyway.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.flywaydb")
                    .artifactId("flyway-mysql")
                    .runtime());
        }

    }
}
