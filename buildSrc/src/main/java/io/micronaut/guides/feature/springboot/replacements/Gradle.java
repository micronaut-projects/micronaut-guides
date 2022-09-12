package io.micronaut.guides.feature.springboot.replacements;

import com.fizzed.rocker.RockerModel;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.feature.springboot.SpringBootApplicationFeature;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.Property;
import io.micronaut.starter.build.gradle.GradleBuild;
import io.micronaut.starter.build.gradle.GradleBuildCreator;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.MicronautRuntimeFeature;
import io.micronaut.starter.feature.build.KotlinBuildPlugins;
import io.micronaut.starter.feature.build.MicronautBuildPlugin;
import io.micronaut.starter.feature.build.gitignore;
import io.micronaut.starter.feature.build.gradle.templates.buildGradle;
import io.micronaut.starter.feature.build.gradle.templates.gradleProperties;
import io.micronaut.starter.feature.build.gradle.templates.settingsGradle;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerTemplate;
import io.micronaut.starter.template.Template;
import io.micronaut.starter.template.URLTemplate;
import jakarta.inject.Singleton;
import io.micronaut.guides.feature.springboot.template.springBootBuildGradle;
import io.micronaut.guides.feature.springboot.template.springBootGitignore;
import io.micronaut.guides.feature.springboot.template.help;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.micronaut.starter.build.Repository.micronautRepositories;

@Singleton
@Replaces(io.micronaut.starter.feature.build.gradle.Gradle.class)
public class Gradle extends io.micronaut.starter.feature.build.gradle.Gradle {

    private static final String WRAPPER_JAR = "gradle/wrapper/gradle-wrapper.jar";
    private static final String WRAPPER_PROPS = "gradle/wrapper/gradle-wrapper.properties";

    private GradleBuildCreator dependencyResolver;

    public Gradle(GradleBuildCreator dependencyResolver,
                  MicronautBuildPlugin micronautBuildPlugin,
                  KotlinBuildPlugins kotlinBuildPlugins) {
        super(dependencyResolver, micronautBuildPlugin, kotlinBuildPlugins);
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addGradleInitFiles(generatorContext);
        extraPlugins(generatorContext).forEach(generatorContext::addBuildPlugin);
        GradleBuild build = createBuild(generatorContext);
        addBuildFile(generatorContext, build);
        addGitignore(generatorContext);
        addProjectPropertiesFile(generatorContext);
        addSettingsFile(generatorContext, build);
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            generatorContext.addTemplate("help", new RockerTemplate(Template.ROOT, "HELP.md", help.template()));
        }
    }

    protected GradleBuild createBuild(GeneratorContext generatorContext) {
        return dependencyResolver.create(generatorContext, micronautRepositories());
    }
    protected void addBuildFile(GeneratorContext generatorContext, GradleBuild build) {
        generatorContext.addTemplate("build", new RockerTemplate(generatorContext.getBuildTool().getBuildFileName(), buildFile(generatorContext, build)));
    }

    protected RockerModel buildFile(GeneratorContext generatorContext, GradleBuild build) {
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            return springBootBuildGradle.template(generatorContext.getProject(), build, generatorContext.getFeatures());
        } {
            return buildGradle.template(
                    generatorContext.getApplicationType(),
                    generatorContext.getProject(),
                    generatorContext.getFeatures(),
                    build
            );
        }
    }

    protected void addGitignore(GeneratorContext generatorContext) {
        generatorContext.addTemplate("gitignore", new RockerTemplate(Template.ROOT, ".gitignore", gitignore(generatorContext)));
    }

    protected RockerModel gitignore(GeneratorContext generatorContext) {
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            return gitignore.template();
        }
        return springBootGitignore.template();
    }

    @NonNull
    private static List<Property> gradleProperties(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getBuildProperties().getProperties().stream()
                .filter(p -> p.getKey() == null || !p.getKey().equals(MicronautRuntimeFeature.PROPERTY_MICRONAUT_RUNTIME)) // It is set via the DSL
                .collect(Collectors.toList());
    }

    protected void addProjectPropertiesFile(GeneratorContext generatorContext) {
        if (!SpringBootApplicationFeature.isSpringBootApplication(generatorContext)) {
            generatorContext.addTemplate("projectProperties", new RockerTemplate(Template.ROOT, "gradle.properties", gradleProperties.template(gradleProperties(generatorContext))));
        }
    }

    protected void addSettingsFile(GeneratorContext generatorContext, GradleBuild build) {
        if (generatorContext.getBuildTool().isGradle()) {
            String settingsFile =  generatorContext.getBuildTool() == BuildTool.GRADLE ? "settings.gradle" : "settings.gradle.kts";
            generatorContext.addTemplate("gradleSettings", new RockerTemplate(Template.ROOT, settingsFile, settingsGradle.template(generatorContext.getProject(), build, generatorContext.getModuleNames())));
        }
    }


    protected List<GradlePlugin> extraPlugins(GeneratorContext generatorContext) {
        List<GradlePlugin> result = new ArrayList<>();
        if (generatorContext.getFeatures().language().isGroovy() || generatorContext.getFeatures().testFramework().isSpock()) {
            result.add(GradlePlugin.builder().id("groovy").build());
        }
        if (SpringBootApplicationFeature.isSpringBootApplication(generatorContext) && generatorContext.getFeatures().language().isJava()) {
            result.add(GradlePlugin.builder().id("java").build());
        }
        return result;
    }

    protected void addGradleInitFiles(GeneratorContext generatorContext) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("gradleWrapperJar", new BinaryTemplate(Template.ROOT, WRAPPER_JAR, classLoader.getResource(WRAPPER_JAR)));
        generatorContext.addTemplate("gradleWrapperProperties", new URLTemplate(Template.ROOT, WRAPPER_PROPS, classLoader.getResource(WRAPPER_PROPS)));
        generatorContext.addTemplate("gradleWrapper", new URLTemplate(Template.ROOT, "gradlew", classLoader.getResource("gradle/gradlew"), true));
        generatorContext.addTemplate("gradleWrapperBat", new URLTemplate(Template.ROOT, "gradlew.bat", classLoader.getResource("gradle/gradlew.bat"), false));
    }
}

