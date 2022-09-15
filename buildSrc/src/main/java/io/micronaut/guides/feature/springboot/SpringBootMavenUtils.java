package io.micronaut.guides.feature.springboot;

import io.micronaut.guides.feature.springboot.template.genericMavenPlugin;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;

public final class SpringBootMavenUtils {
    private SpringBootMavenUtils() {
    }

    public static void addJavaVersionProperty(GeneratorContext generatorContext) {
        generatorContext.getBuildProperties().put("java.version", generatorContext.getFeatures().getTargetJdk());
    }

    public static void clearMicronautVersionProperty(GeneratorContext generatorContext) {
        generatorContext.getBuildProperties().remove("micronaut.version");
    }

    public static void addSpringBootMavenPlugin(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .artifactId("spring-boot-maven-plugin")
                .extension(new RockerWritable(genericMavenPlugin.template("org.springframework.boot", "spring-boot-maven-plugin")))
                .build());
    }
}
