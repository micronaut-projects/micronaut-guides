package io.micronaut.guides.feature.springboot.replacements;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.guides.feature.springboot.template.springBootGenericPom;
import io.micronaut.guides.feature.springboot.template.springBootGitignore;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenBuild;
import io.micronaut.starter.build.maven.MavenBuildCreator;
import io.micronaut.starter.feature.build.maven.Maven;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

import static io.micronaut.guides.feature.springboot.SpringBootApplicationFeature.isSpringBootApplication;

@Replaces(Maven.class)
@Singleton
public class MavenReplacement extends Maven {
    protected final CoordinateResolver coordinateResolver;

    public MavenReplacement(MavenBuildCreator dependencyResolver, CoordinateResolver coordinateResolver) {
        super(dependencyResolver);
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    protected RockerModel gitIgnore(GeneratorContext generatorContext) {
        if (isSpringBootApplication(generatorContext)) {
            return springBootGitignore.template();
        }
        return super.gitIgnore(generatorContext);
    }

    @Override
    protected RockerModel pom(GeneratorContext generatorContext, MavenBuild mavenBuild) {
        if (isSpringBootApplication(generatorContext)) {
            Coordinate springBootParent = coordinateResolver.resolve("spring-boot-starter-parent").orElseThrow();
            String sourceDirectory = generatorContext.getLanguage() == Language.KOTLIN ? "${project.basedir}/src/main/kotlin" : null;
            String testSourceDirectory = generatorContext.getLanguage() == Language.KOTLIN ? "${project.basedir}/src/test/kotlin" : null;
            return springBootGenericPom.template(generatorContext.getProject(),
                    mavenBuild,
                    springBootParent.getGroupId(),
                    springBootParent.getArtifactId(),
                    springBootParent.getVersion(),
                    true,
                    sourceDirectory,
                    testSourceDirectory);
        }
        return super.pom(generatorContext, mavenBuild);
    }
}
