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
        super.apply(generatorContext);
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            SpringBootMavenUtils.addJavaVersionProperty(generatorContext);
            SpringBootMavenUtils.addSpringBootMavenPlugin(generatorContext);
            addGmavenPlusMavenPlugin(generatorContext);
        }
    }

    private void addGmavenPlusMavenPlugin(GeneratorContext generatorContext) {
        coordinateResolver.resolve("gmavenplus-plugin").ifPresent(c ->
                generatorContext.addBuildPlugin(MavenPlugin.builder()
                        .artifactId("gmavenplus-plugin")
                        .extension(new RockerWritable(gmavenPlusMavenPlugin.template(c.getGroupId(), c.getArtifactId(), c.getVersion())))
                        .build()));
    }

    @Override
    protected RockerModel application(GeneratorContext generatorContext) {
        return applicationgroovy.template(generatorContext.getProject(), generatorContext.getFeatures());
    }

    @Override
    protected RockerModel applicationTest(GeneratorContext generatorContext) {
        return applicationtestgroovyjunit.template(generatorContext.getProject());
    }
}
