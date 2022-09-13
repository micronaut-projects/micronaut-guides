package io.micronaut.guides.feature.springboot;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.template.application;
import io.micronaut.guides.feature.springboot.template.applicationtestjavajunit;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.lang.java.JavaApplication;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class SpringBootJavaApplication extends JavaApplication implements SpringBootApplicationFeature {

    @Override
    @NonNull
    public String getName() {
        return "spring-boot-java-application";
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
            }
        }
    }

    protected void generateApplication(GeneratorContext generatorContext) {
        application(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("application", new RockerTemplate(getPath(), rockerModel));
        });
    }

    protected Optional<RockerModel> application(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.JAVA) {
            return Optional.of(application.template(generatorContext.getProject(), generatorContext.getFeatures()));
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
        if (generatorContext.getTestFramework() == TestFramework.JUNIT && generatorContext.getLanguage() == Language.JAVA) {
            return Optional.of(applicationtestjavajunit.template(generatorContext.getProject()));
        }
        return Optional.empty();
    }
}
