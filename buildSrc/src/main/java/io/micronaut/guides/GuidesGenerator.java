package io.micronaut.guides;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.Project;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.generator.ProjectGenerator;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.io.FileSystemOutputHandler;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.util.NameUtils;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.JdkVersion.JDK_8;

@Singleton
public class GuidesGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(GuidesGenerator.class);

    private final ProjectGenerator projectGenerator;

    public GuidesGenerator(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

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
        GeneratorContext generatorContext = createProjectGeneratorContext(type, packageAndName, framework, features, buildTool, testFramework, lang, javaVersion);
        try {
            projectGenerator.generate(type,
                    generatorContext.getProject(),
                    new FileSystemOutputHandler(directory, ConsoleOutput.NOOP),
                    generatorContext);
        } catch (Exception e) {
            LOG.error("Error generating application: " + e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }
    }

    GeneratorContext createProjectGeneratorContext(
            ApplicationType type,
            @Pattern(regexp = "[\\w\\d-_\\.]+") String packageAndName,
            @Nullable String framework,
            @Nullable List<String> features,
            @Nullable BuildTool buildTool,
            @Nullable TestFramework testFramework,
            @Nullable Language lang,
            @Nullable JdkVersion javaVersion) throws IllegalArgumentException {
        Project project;
        try {
            project = NameUtils.parse(packageAndName);
        } catch (IllegalArgumentException e) {
            throw new HttpStatusException(BAD_REQUEST, "Invalid project name: " + e.getMessage());
        }

        return projectGenerator.createGeneratorContext(
                type,
                project,
                new Options(
                        lang,
                        testFramework != null ? testFramework.toTestFramework() : null,
                        buildTool == null ? GRADLE : buildTool,
                        javaVersion != null ? javaVersion : JDK_8).withFramework(framework),
                null,
                features != null ? features : Collections.emptyList(),
                ConsoleOutput.NOOP
        );
    }
}
