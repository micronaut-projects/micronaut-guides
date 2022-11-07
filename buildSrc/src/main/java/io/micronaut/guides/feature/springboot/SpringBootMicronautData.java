package io.micronaut.guides.feature.springboot;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.database.H2;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class SpringBootMicronautData implements SpringBootApplicationFeature {

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
        return "Adds Micronatu Data to a Spring Boot application";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        String micronautVersion = VersionInfo.getMicronautVersion();
        generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-bom")
                .version(micronautVersion)
                .versionProperty(MICRONAUT_VERSION)
                .compile()
                .pom());
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                    .artifactId("micronaut-bom")
                    .version(micronautVersion)
                    .versionProperty(MICRONAUT_VERSION)
                    .annotationProcessor()
                    .pom());
        }
        generatorContext.addDependency(MicronautDependencyUtils.dataDependency()
                .artifactId("micronaut-data-processor")
                .version(VersionInfo.getBomVersion("micronaut.data"))
                .annotationProcessor());

        generatorContext.addDependency(MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-inject-java")
                .versionProperty(MICRONAUT_VERSION)
                .annotationProcessor());

        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("jakarta.annotation-api")
                .annotationProcessor());

        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("jakarta.annotation-api")
                .compile());

        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("jakarta.persistence-api")
                .annotationProcessor());

        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("jakarta.persistence-api")
                .compile());

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
            Map<String, Object> jdbcConfig = new LinkedHashMap<>();
            jdbcConfig.put("datasources.default.url", "jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
            jdbcConfig.put("datasources.default.username", "sa");
            jdbcConfig.put("datasources.default.password", "");
            jdbcConfig.put("datasources.default.driverClassName", "org.h2.Driver");
            jdbcConfig.put("datasources.default.schema-generate", "CREATE_DROP");
            jdbcConfig.put("datasources.default.dialect", "H2");
            generatorContext.getConfiguration().addNested(jdbcConfig);
        }

    }
}
