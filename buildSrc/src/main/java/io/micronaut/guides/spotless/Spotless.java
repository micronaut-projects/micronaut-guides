package io.micronaut.guides.spotless;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.guides.feature.Geb;
import io.micronaut.guides.spotless.templates.spotlessGradle;
import io.micronaut.guides.spotless.templates.spotlessMaven;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.build.Develocity;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.template.BinaryTemplate;
import io.micronaut.starter.template.RockerWritable;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
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
    public int getOrder() {
        return FeaturePhase.BUILD_PLUGIN.getOrder();
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
                        languages(generatorContext))))
                .build();
    }

    private List<Language> languages(GeneratorContext generatorContext) {
        List<Language> languages = new ArrayList<>();
        languages.add(generatorContext.getLanguage());
        if (generatorContext.hasFeature(Geb.class)) {
            languages.add(Language.GROOVY);
        }
        return languages;
    }


    private GradlePlugin gradleBuildPlugin(GeneratorContext generatorContext) {
        GradlePlugin.Builder gradlePlugin = GradlePlugin.builder()
                .id("com.diffplug.spotless");
        // If we are applying the MicronautGradle Enterprise plugin, we don't need to add the spotless plugin version (as it is already included)
        if (!generatorContext.hasFeature(Develocity.class)) {
            gradlePlugin.lookupArtifactId("spotless-plugin-gradle");
        }
        return gradlePlugin
                .extension(new RockerWritable(spotlessGradle.template(languages(generatorContext))))
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
