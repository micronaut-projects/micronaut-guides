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
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.util.Optional;

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
        if (shouldGenerateApplicationFile(generatorContext)) {
            generateApplication(generatorContext);
            generateApplicationTest(generatorContext);
        }
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            SpringBootMavenUtils.clearMicronautVersionProperty(generatorContext);
            if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
                SpringBootMavenUtils.addJavaVersionProperty(generatorContext);
                addKotlinVersionProperty(generatorContext);
                SpringBootMavenUtils.addSpringBootMavenPlugin(generatorContext);
                addSpringKotlinMavenPlugin(generatorContext);
            }
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

    protected void generateApplication(GeneratorContext generatorContext) {
        application(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("application", new RockerTemplate(getPath(), rockerModel));
        });
    }

    protected Optional<RockerModel> application(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.of(applicationkotlin.template(generatorContext.getProject()));
        }
        return Optional.empty();
    }

    protected void generateApplicationTest(GeneratorContext generatorContext) {
        applicationTest(generatorContext).ifPresent(rockerModel -> {
            String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/{className}");
            generatorContext.addTemplate("applicationTest", new RockerTemplate(testSourcePath, rockerModel));
        });
    }

    protected Optional<RockerModel> applicationTest(GeneratorContext generatorContext) {
        if (generatorContext.getTestFramework() == TestFramework.JUNIT && generatorContext.getLanguage() == Language.KOTLIN) {
            return Optional.of(applicationtestkotlinjunit.template(generatorContext.getProject()));
        }
        return Optional.empty();
    }
}
