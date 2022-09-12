package io.micronaut.guides.feature.springboot;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.template.application;
import io.micronaut.guides.feature.springboot.template.applicationtestjavajunit;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.lang.java.JavaApplication;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.RockerTemplate;
import jakarta.inject.Singleton;

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
    }

    protected void generateApplication(GeneratorContext generatorContext) {
        generatorContext.addTemplate("application", new RockerTemplate(getPath(),
                application.template(generatorContext.getProject(), generatorContext.getFeatures())));
    }

    protected void generateApplicationTest(GeneratorContext generatorContext) {
        String testSourcePath = generatorContext.getTestSourcePath("/{packagePath}/{className}");
        if (generatorContext.getLanguage() == Language.JAVA && generatorContext.getTestFramework() == TestFramework.JUNIT) {
            generatorContext.addTemplate("applicationTest", new RockerTemplate(testSourcePath,
                    applicationtestjavajunit.template(generatorContext.getProject())));
        }
    }
}
