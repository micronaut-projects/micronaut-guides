package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.build.gradle.MicronautTestResourcesGradlePlugin;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class TestResourcesLongTimeout implements Feature {
    private static final String NAME = "test-resources-long-timeout";
    private static final String CLIENT_TIMEOUT_SECONDS = "600";
    private static final String MAVEN_CLIENT_TIMEOUT_PROPERTY = "micronaut.test.resources.client-timeout";
    private static final String TEST_RESOURCES_PLUGIN_ID = MicronautTestResourcesGradlePlugin.Builder.MICRONAUT_GRADLE_PLUGIN_TEST_RESOURCES_ID;
    private static final String TEST_RESOURCES_PLUGIN_ARTIFACT_ID = MicronautTestResourcesGradlePlugin.Builder.ARTIFACT_ID;

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.DEFAULT.getOrder() + 1;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        BuildTool buildTool = generatorContext.getBuildTool();
        if (buildTool.isGradle()) {
            replaceTestResourcesGradlePlugin(generatorContext, buildTool);
        } else if (buildTool == BuildTool.MAVEN) {
            generatorContext.getBuildProperties().put(MAVEN_CLIENT_TIMEOUT_PROPERTY, CLIENT_TIMEOUT_SECONDS);
        }
    }

    private static void replaceTestResourcesGradlePlugin(GeneratorContext generatorContext, BuildTool buildTool) {
        Set<BuildPlugin> buildPlugins = generatorContext.getBuildPlugins();
        buildPlugins.removeIf(plugin -> plugin instanceof GradlePlugin gradlePlugin
            && TEST_RESOURCES_PLUGIN_ID.equals(gradlePlugin.getId()));
        generatorContext.addBuildPlugin(GradlePlugin.builder()
            .id(TEST_RESOURCES_PLUGIN_ID)
            .lookupArtifactId(TEST_RESOURCES_PLUGIN_ARTIFACT_ID)
            .extension(gradleTestResourcesExtension(buildTool))
            .build());
    }

    private static String gradleTestResourcesExtension(BuildTool buildTool) {
        String clientTimeout = buildTool == BuildTool.GRADLE_KOTLIN
            ? "clientTimeout.set(%s)".formatted(CLIENT_TIMEOUT_SECONDS)
            : "clientTimeout = %s".formatted(CLIENT_TIMEOUT_SECONDS);
        return """
            micronaut {
                testResources {
                    %s
                }
            }
            """.formatted(clientTimeout);
    }
}
