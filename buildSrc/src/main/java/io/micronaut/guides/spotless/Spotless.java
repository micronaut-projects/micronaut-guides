package io.micronaut.guides.spotless;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.guides.spotless.templates.spotlessGradle;
import io.micronaut.guides.spotless.templates.spotlessMaven;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

import javax.swing.text.html.Option;
import java.util.Optional;

@Singleton
public class Spotless implements Feature {
    @Override
    public @NonNull String getName() {
        return "spotless";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getTitle() {
        return "Spotless";
    }

    @Override
    public String getDescription() {
        return "Adds Spotless build plugins";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        buildPlugin(generatorContext).ifPresent(generatorContext::addBuildPlugin);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        generatorContext.addTemplate("LICENSEHEADER", new BinaryTemplate(Template.ROOT, "LICENSEHEADER", classLoader.getResource("LICENSEHEADER")));
    }

    private Optional<BuildPlugin> buildPlugin(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            return Optional.of(gradleBuildPlugin(generatorContext));
        } else if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            return Optional.of(mavenBuildPlugin(generatorContext));
        }
        return Optional.empty();
    }

    private MavenPlugin mavenBuildPlugin(GeneratorContext generatorContext) {
        Coordinate coordinate = generatorContext.resolveCoordinate("spotless-maven-plugin");
        return MavenPlugin.builder()
                .artifactId("spotless-maven-plugin")
                .extension(new RockerWritable(spotlessMaven.template(coordinate.getGroupId(),
                        coordinate.getArtifactId(),
                        coordinate.getVersion(),
                        generatorContext.getLanguage())))
                .build();
    }

    private GradlePlugin gradleBuildPlugin(GeneratorContext generatorContext) {
        return GradlePlugin.builder()
                .id("com.diffplug.spotless")
                .lookupArtifactId("spotless-plugin-gradle")
                .extension(new RockerWritable(spotlessGradle.template(generatorContext.getLanguage())))
                .build();
    }

    @Override
    public @Nullable String getThirdPartyDocumentation() {
        return "https://github.com/diffplug/spotless";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
