package io.micronaut.guides.feature.springboot;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.template.applicationgroovy;
import io.micronaut.guides.feature.springboot.template.applicationtestgroovyjunit;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.lang.groovy.GroovyApplication;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class SpringBootGroovyApplication extends GroovyApplication implements SpringBootApplicationFeature {
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
    }

    protected void generateApplication(GeneratorContext generatorContext) {
        application(generatorContext).ifPresent(rockerModel -> {
            generatorContext.addTemplate("application", new RockerTemplate(getPath(), rockerModel));
        });
    }

    protected Optional<RockerModel> application(GeneratorContext generatorContext) {
        if (generatorContext.getLanguage() == Language.GROOVY) {
            return Optional.of(applicationgroovy.template(generatorContext.getProject(), generatorContext.getFeatures()));
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
        if (generatorContext.getTestFramework() == TestFramework.JUNIT && generatorContext.getLanguage() == Language.GROOVY) {
            return Optional.of(applicationtestgroovyjunit.template(generatorContext.getProject()));
        }
        return Optional.empty();
    }
}
