package io.spring.start;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.feature.build.gradle.Gradle;
import io.micronaut.starter.build.RepositoryResolver;
import io.micronaut.starter.build.gradle.GradleBuildCreator;
import jakarta.inject.Singleton;
import io.micronaut.starter.application.generator.GeneratorContext;

@Replaces(Gradle.class)
@Singleton
public class GradleReplacement extends Gradle {

    public GradleReplacement(GradleBuildCreator dependencyResolver,
                  RepositoryResolver repositoryResolver) {
        super(dependencyResolver, repositoryResolver);
    }

    protected void generateNoneMicronautFrameworkBuild(GeneratorContext generatorContext) {
        super.generateNoneMicronautFrameworkBuild(generatorContext);
        addGradleInitFiles(generatorContext);
    }
}
