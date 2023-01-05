package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.guides.feature.template.appengineGradle;
import io.micronaut.guides.feature.template.appengineGradleSettings;
import io.micronaut.guides.feature.template.appengineMaven;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import static io.micronaut.starter.feature.Category.CLOUD;

@Singleton
public class GoogleAppEngine implements Feature {
    private final CoordinateResolver coordinateResolver;

    public GoogleAppEngine(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public String getCategory() {
        return CLOUD;
    }

    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://github.com/GoogleCloudPlatform/app-gradle-plugin";
    }

    @NonNull
    @Override
    public String getTitle() {
        return "Google App Engine Gradle Plugin";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds a Gradle plugin provides tasks to build and deploy Google App Engine applications.";
    }

    @NonNull
    @Override
    public String getName() {
        return "google-app-engine-gradle";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("com.google.cloud.tools.appengine")
                    .lookupArtifactId("appengine-gradle-plugin")
                            .extension(new RockerWritable(appengineGradle.template()))
                            .settingsExtension(new RockerWritable(appengineGradleSettings.template()))
                    .build());
        } else if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            String mavenPluginArtifactId = "appengine-maven-plugin";
            BuildProperties props = generatorContext.getBuildProperties();
            coordinateResolver.resolve(mavenPluginArtifactId)
                    .ifPresent(coordinate -> props.put("appengine.maven.plugin.version", coordinate.getVersion()));
            generatorContext.addBuildPlugin(MavenPlugin.builder()
                    .artifactId(mavenPluginArtifactId)
                    .extension(new RockerWritable(appengineMaven.template()))
                    .build());

        }
    }
}
