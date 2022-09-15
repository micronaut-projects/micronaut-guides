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
        super.apply(generatorContext);
        SpringBootMavenUtils.clearMicronautVersionProperty(generatorContext);
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            SpringBootMavenUtils.addJavaVersionProperty(generatorContext);
            SpringBootMavenUtils.addSpringBootMavenPlugin(generatorContext);
        }
    }

    @Override
    protected RockerModel application(GeneratorContext generatorContext) {
        return application.template(generatorContext.getProject(), generatorContext.getFeatures());
    }

    @Override
    protected RockerModel applicationTest(GeneratorContext generatorContext) {
        return applicationtestjavajunit.template(generatorContext.getProject());
    }
}
