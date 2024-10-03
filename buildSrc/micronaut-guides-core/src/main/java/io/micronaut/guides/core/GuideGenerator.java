package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface GuideGenerator {

    /**
     * Generates an application into a given directory.
     *
     * @param directory        The directory where the generated application will be saved.
     * @param type             The type of application to generate.
     * @param packageAndName   The full name of the package and the main class of the application.
     * @param framework        Optional parameter specifying the testing framework to use.
     * @param features         Optional list of additional features to include in the application.
     * @param buildTool        Optional parameter specifying the build tool to use.
     * @param testFramework    Optional parameter specifying the testing framework to use.
     * @param lang             Optional parameter specifying the programming language to use.
     * @param javaVersion      Optional parameter specifying the version of Java to use.
     * @throws IOException If there is an error while generating the application or saving it to the directory.
     */
    public void generateAppIntoDirectory(
            @NonNull File directory,
            @NotNull ApplicationType type,
            @NotNull String packageAndName,
            @Nullable String framework,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion) throws IOException;
}
