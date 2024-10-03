package io.micronaut.guides.core

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.starter.api.TestFramework
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import jakarta.inject.Singleton
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern;

@Singleton
public class DefaultGuideGenerator implements GuideGenerator {
    @Override
    public void generateAppIntoDirectory(
            @NonNull File directory,
            @NotNull ApplicationType type,
            @NotNull String packageAndName,
            @Nullable String framework,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion) throws IOException {
        throw new UnsupportedOperationException("No custom GuideGenerator implementation found");
    }
}