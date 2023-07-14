package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.database.H2;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Map;

import static io.micronaut.starter.application.ApplicationType.DEFAULT;
import static io.micronaut.starter.feature.Category.DATABASE;

@Singleton
public class SpringBootMicronautData implements Feature {

    public static final String MICRONAUT_VERSION = "micronaut.version";

    @Override
    @NonNull
    public String getName() {
        return "spring-boot-micronaut-data";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data Spring Boot";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Micronaut Data to a Spring Boot application";
    }

    @Override
    public String getCategory() {
        return DATABASE;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        String micronautVersion = VersionInfo.getMicronautVersion();
        generatorContext.addDependency(MicronautDependencyUtils.platformDependency()
                .artifactId("micronaut-platform")
                .version(micronautVersion)
                .versionProperty(MICRONAUT_VERSION)
                .compile()
                .pom());
        generatorContext.addDependency(MicronautDependencyUtils.dataDependency()
                .artifactId("micronaut-data-processor")
                .version(VersionInfo.getBomVersion("micronaut.data"))
                .annotationProcessor());

        generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-inject-java")
                .versionProperty(MICRONAUT_VERSION)
                .annotationProcessor());

        generatorContext.addDependency(Dependency.builder()
                .groupId("jakarta.annotation")
                .artifactId("jakarta.annotation-api")
                        .scope(Scope.COMPILE)
                        .build());

        generatorContext.addDependency(Dependency.builder()
                .groupId("jakarta.persistence")
                .artifactId("jakarta.persistence-api")
                        .scope(Scope.COMPILE)
                .build());

        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.spring")
                .artifactId("micronaut-spring-boot-starter")
                .compile());

        generatorContext.addDependency(MicronautDependencyUtils.dataDependency()
                .artifactId("micronaut-data-jdbc")
                .compile());

        generatorContext.addDependency(MicronautDependencyUtils.sqlDependency()
                .artifactId("micronaut-jdbc-hikari")
                .compile());

        if (generatorContext.getFeatures().isFeaturePresent(H2.class)) {
            generatorContext.getConfiguration().addNested(Map.of(
                    "datasources.default.url", "jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE",
                    "datasources.default.username", "sa",
                    "datasources.default.password", "",
                    "datasources.default.driverClassName", "org.h2.Driver",
                    "datasources.default.schema-generate", "CREATE_DROP",
                    "datasources.default.dialect", "H2"));
        }

    }
}
