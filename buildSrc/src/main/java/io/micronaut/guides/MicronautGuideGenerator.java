package io.micronaut.guides;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.guides.core.GuideGenerator;
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

/**
 * The GuidesGenerator class generates Micronaut applications based on provided parameters and saves them to a specified directory.
 */
@Singleton
public class MicronautGuideGenerator implements GuideGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(MicronautGuideGenerator.class);

    /**
     * An instance of the ProjectGenerator interface responsible for creating projects.
     */
    private final ProjectGenerator projectGenerator;

    /**
     * Constructor for GuidesGenerator.
     *
     * @param projectGenerator An instance of the ProjectGenerator interface.
     */
    public MicronautGuideGenerator(ProjectGenerator projectGenerator) {
        this.projectGenerator = projectGenerator;
    }

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

    /**
     * Creates a GeneratorContext object containing all necessary information about the application being generated.
     *
     * @param type             The type of application to generate.
     * @param packageAndName   The full name of the package and the main class of the application.
     * @param framework        Optional parameter specifying the testing framework to use.
     * @param features         Optional list of additional features to include in the application.
     * @param buildTool        Optional parameter specifying the build tool to use.
     * @param testFramework    Optional parameter specifying the testing framework to use.
     * @param lang             Optional parameter specifying the programming language to use.
     * @param javaVersion      Optional parameter specifying the version of Java to use.
     * @return A GeneratorContext object populated with the provided parameters.
     * @throws IllegalArgumentException If the provided packageAndName does not conform to the expected format.
     */
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
