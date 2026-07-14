package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class LogbackTest implements Feature {
    public static final String GROUP_ID_LOGBACK = "ch.qos.logback";
    public static final String ARTIFACT_ID_LOGBACK_CLASSIC = "logback-classic";
    private static final Dependency LOGBACK_CLASSIC_TEST = Dependency.builder()
            .groupId(GROUP_ID_LOGBACK)
            .artifactId(ARTIFACT_ID_LOGBACK_CLASSIC)
            .test()
            .build();

    @Override
    public @NonNull String getName() {
        return "logback-test";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(LOGBACK_CLASSIC_TEST);
    }
}
