package io.spring.start;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.feature.KotlinSymbolProcessing;
import io.micronaut.starter.feature.build.gradle.Gradle;
import io.micronaut.starter.build.RepositoryResolver;
import io.micronaut.starter.build.gradle.GradleBuildCreator;
import jakarta.inject.Singleton;
import io.micronaut.starter.application.generator.GeneratorContext;

import java.util.ArrayList;
import java.util.List;

@Replaces(Gradle.class)
@Singleton
public class GradleReplacement extends Gradle {
    private static final Property PROPERTY_GRADLE_JVMARGS_4G = new Property() {
        @Override
        public String getKey() {
            return "org.gradle.jvmargs";
        }

        @Override
        public String getValue() {
            return "-Xmx4G";
        }
    };
    public GradleReplacement(GradleBuildCreator dependencyResolver,
                  RepositoryResolver repositoryResolver) {
        super(dependencyResolver, repositoryResolver);
    }

    protected void generateNoneMicronautFrameworkBuild(GeneratorContext generatorContext) {
        super.generateNoneMicronautFrameworkBuild(generatorContext);
        addGradleInitFiles(generatorContext);
    }

    @NonNull
    @Override
    protected List<Property> gradleProperties(@NonNull GeneratorContext generatorContext) {
        List<Property> properties = new ArrayList(generatorContext.getBuildProperties().getProperties().stream().filter((p) -> {
            return p.getKey() == null || !p.getKey().equals("micronaut.runtime");
        }).toList());
        properties.add(PROPERTY_GRADLE_JVMARGS_4G);
        return properties;
    }
}
