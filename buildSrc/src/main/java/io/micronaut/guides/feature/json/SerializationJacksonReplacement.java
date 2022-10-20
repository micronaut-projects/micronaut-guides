package io.micronaut.guides.feature.json;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.json.SerializationJacksonFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Replaces SerializationJacksonFeature.
 * <p>
 * Required to fix <a href="https://github.com/micronaut-projects/micronaut-starter/pull/1491">this issue with Maven, Serde and Test Resources</a>
 * <p>
 * Can be removed once 3.7.3 is released with that fix.
 */
@Singleton
@Replaces(SerializationJacksonFeature.class)
public class SerializationJacksonReplacement extends SerializationJacksonFeature {

    @Override
    @NonNull
    public List<Dependency.Builder> dependencies(@NonNull GeneratorContext generatorContext) {
        List<Dependency.Builder> dependencyList = new ArrayList<>();
        dependencyList.add(serdeProcessor());
        dependencyList.add(serdeModule(generatorContext));
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            dependencyList.add(micronautRuntimeDependency(generatorContext));
        }
        return dependencyList;
    }

    @NonNull
    Dependency.Builder micronautRuntimeDependency(@NonNull GeneratorContext generatorContext) {
        Dependency.Builder runtime = MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-runtime")
                .compile();
        if (!generatorContext.isFeaturePresent(TestResources.class)) {
            runtime.exclude(DEPENDENCY_MICRONAUT_JACKSON_DATABIND);
        }
        return runtime;
    }
}
