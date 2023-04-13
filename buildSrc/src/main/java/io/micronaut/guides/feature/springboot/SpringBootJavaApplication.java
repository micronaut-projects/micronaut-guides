package io.micronaut.guides.feature.springboot;

import com.fizzed.rocker.RockerModel;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.template.application;
import io.micronaut.guides.feature.springboot.template.applicationtestjavajunit;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.lang.java.JavaApplication;
import jakarta.inject.Singleton;

import static io.micronaut.starter.options.BuildTool.MAVEN;

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
        if (generatorContext.getBuildTool() == MAVEN) {
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
