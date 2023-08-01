package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import io.spring.start.templates.testcontainersJooqCodegenMavenPlugin;
import jakarta.inject.Singleton;

@Singleton
public class TestContainersJooqCodegenMavenPlugin implements Feature {
    @Override
    @NonNull
    public String getName() {
        return "testcontainers-jooq-codegen-maven-plugin.version";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {

            BuildProperties buildProperties = generatorContext.getBuildProperties();
            buildProperties.put("testcontainers-jooq-codegen-maven-plugin.version", "0.0.2");
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .groupId("org.testcontainers")
                    .artifactId("testcontainers-jooq-codegen-maven-plugin")
                    .extension(new RockerWritable(testcontainersJooqCodegenMavenPlugin.template(generatorContext.getProject(), "42.6.0")))
                    .build());
        }
    }
}
