package io.micronaut.guides.feature.springboot;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.template.applicationgroovy;
import io.micronaut.guides.feature.springboot.template.applicationtestgroovyjunit;
import io.micronaut.guides.feature.springboot.template.gmavenPlusMavenPlugin;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.lang.groovy.GroovyApplication;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class SpringBootGroovyApplication extends GroovyApplication implements SpringBootApplicationFeature {

    private final CoordinateResolver coordinateResolver;

    public SpringBootGroovyApplication(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    @NonNull
    public String getName() {
        return "spring-boot-groovy-application";
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
                SpringBootMavenUtils.addSpringBootMavenPlugin(generatorContext);
                addGmavenPlusMavenPlugin(generatorContext);
            }
        }
    }

    private void addGmavenPlusMavenPlugin(GeneratorContext generatorContext) {
        coordinateResolver.resolve("gmavenplus-plugin").ifPresent(c ->
                generatorContext.addBuildPlugin(MavenPlugin.builder()
                        .artifactId("gmavenplus-plugin")
                        .extension(new RockerWritable(gmavenPlusMavenPlugin.template(c.getGroupId(), c.getArtifactId(), c.getVersion())))
                        .build()));
    }

    protected void generateApplication(GeneratorContext generatorContext) {
        generateRockerModel(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("application", new RockerTemplate(getPath(), rockerModel));
        });
    }

    protected Optional<RockerModel> generateRockerModel(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.GROOVY) {
            return Optional.of(applicationgroovy.template(generatorContext.getProject(), generatorContext.getFeatures()));
        }
        return Optional.empty();
    }

    protected void generateApplicationTest(GeneratorContext generatorContext) {
        generateTestRockerModel(generatorContext).ifPresent(rockerModel -> {
            String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/{className}");
            generatorContext.addTemplate("applicationTest", new RockerTemplate(testSourcePath, rockerModel));
        });
    }

    protected Optional<RockerModel> generateTestRockerModel(GeneratorContext generatorContext) {
        if (generatorContext.getTestFramework() == TestFramework.JUNIT && generatorContext.getLanguage() == Language.GROOVY) {
            return Optional.of(applicationtestgroovyjunit.template(generatorContext.getProject()));
        }
        return Optional.empty();
    }
}
