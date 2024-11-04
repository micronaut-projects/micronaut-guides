package io.micronaut.guides.core;

import io.micronaut.context.exceptions.ConfigurationException;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.micronaut.core.util.StringUtils.EMPTY_STRING;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.JdkVersion.JDK_8;

@Singleton
public class DefaultGuideProjectGenerator implements GuideProjectGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGuideProjectGenerator.class);
    private final GuidesConfiguration guidesConfiguration;
    private final ProjectGenerator projectGenerator;

    DefaultGuideProjectGenerator(GuidesConfiguration guidesConfiguration,
                                 ProjectGenerator projectGenerator) {
        this.guidesConfiguration = guidesConfiguration;
        this.projectGenerator = projectGenerator;
    }

    @Override
    public void generate(@NotNull @NonNull File outputDirectory, @NotNull @NonNull Guide guide) throws IOException {
        if (!outputDirectory.exists()) {
            throw new ConfigurationException("Output directory does not exist");
        }
        if (!outputDirectory.isDirectory()) {
            throw new ConfigurationException("Output directory must be a directory");
        }

        JdkVersion javaVersion = GuideGenerationUtils.resolveJdkVersion(guidesConfiguration, guide);
        if (guide.maximumJavaVersion() != null && javaVersion.majorVersion() > guide.maximumJavaVersion()) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("not generating project for {}, JDK {} > {}", guide.slug(), javaVersion.majorVersion(), guide.maximumJavaVersion());
            }
            return;
        }

        List<GuidesOption> guidesOptionList = GuideGenerationUtils.guidesOptions(guide,LOG);
        for (GuidesOption guidesOption : guidesOptionList) {
            BuildTool buildTool = guidesOption.getBuildTool();
            TestFramework testFramework = guidesOption.getTestFramework();
            Language lang = guidesOption.getLanguage();

            for (App app : guide.apps()) {
                List<String> appFeatures = new ArrayList<>(GuideUtils.getAppFeatures(app, lang));
                if (!guidesConfiguration.getJdkVersionsSupportedByGraalvm().contains(javaVersion)) {
                    appFeatures.remove("graalvm");
                }

                if (testFramework == TestFramework.SPOCK) {
                    appFeatures.remove("mockito");
                }

                // typical guides use 'default' as name, multi-project guides have different modules
                String folder = MacroUtils.getSourceDir(guide.slug(), guidesOption);

                String appName = app.name().equals(guidesConfiguration.getDefaultAppName()) ? EMPTY_STRING : app.name();

                Path destinationPath = Paths.get(outputDirectory.getAbsolutePath(), folder, appName);
                File destination = destinationPath.toFile();
                destination.mkdir();

                String packageAndName = guidesConfiguration.getPackageName() + '.' + app.name();

                generateAppIntoDirectory(destination, app.applicationType(), packageAndName, app.framework(),
                        appFeatures, buildTool, app.testFramework() != null ? app.testFramework() : testFramework, lang, javaVersion);
            }
        }
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
