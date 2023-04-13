package io.micronaut.guides.feature.springboot;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.template.applicationkotlin;
import io.micronaut.guides.feature.springboot.template.applicationtestkotlinjunit;
import io.micronaut.guides.feature.springboot.template.springKotlinMavenPlugin;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.lang.kotlin.KotlinApplication;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import static io.micronaut.starter.options.BuildTool.MAVEN;

@Singleton
public class SpringBootKotlinApplication extends KotlinApplication implements SpringBootApplicationFeature {
    private final CoordinateResolver coordinateResolver;

    public SpringBootKotlinApplication(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "spring-boot-kotlin-application";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        if (generatorContext.getBuildTool() == MAVEN) {
            SpringBootMavenUtils.addJavaVersionProperty(generatorContext);
            addKotlinVersionProperty(generatorContext);
            SpringBootMavenUtils.addSpringBootMavenPlugin(generatorContext);
            addSpringKotlinMavenPlugin(generatorContext);
        }
    }

    private  void addKotlinVersionProperty(GeneratorContext generatorContext) {
        coordinateResolver.resolve("kotlin-allopen").ifPresent(coordinate ->
                generatorContext.getBuildProperties().put("kotlin.version", coordinate.getVersion()));
    }

    private void addSpringKotlinMavenPlugin(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .artifactId("kotlin-maven-plugin")
                .extension(new RockerWritable(springKotlinMavenPlugin.template()))
                .build());
    }

    @Override
    protected RockerModel application(GeneratorContext generatorContext) {
        return applicationkotlin.template(generatorContext.getProject());
    }

    @Override
    protected RockerModel applicationTest(GeneratorContext generatorContext) {
        return applicationtestkotlinjunit.template(generatorContext.getProject());
    }
}
