package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.build.gradle.MicronautTestResourcesGradlePlugin;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.StringTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Singleton
public class TestResourcesLongTimeout implements Feature {
    static final String CLIENT_TIMEOUT_SECONDS = "600";
    private static final String NAME = "test-resources-long-timeout";
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
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getBuildTool() == BuildTool.PYRONAUT) {
            // pyproject.toml is rendered by the starter's build feature in the BUILD phase, after this
            // feature has run, so the Pyronaut timeout is applied by a feature ordered after it.
            featureContext.addFeature(new PyronautClientTimeout());
        }
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

    /**
     * Sets {@code tool.pyronaut.test-resources.client-timeout} in the generated {@code pyproject.toml},
     * the Pyronaut equivalent of the Gradle {@code clientTimeout} and Maven {@code client-timeout} settings.
     */
    static final class PyronautClientTimeout implements Feature {
        static final String PYPROJECT_TEMPLATE = "pyproject";
        static final String TEST_RESOURCES_TABLE = "[tool.pyronaut.test-resources]";
        static final String CLIENT_TIMEOUT_LINE = "client-timeout = " + CLIENT_TIMEOUT_SECONDS;

        @Override
        @NonNull
        public String getName() {
            return NAME + "-pyronaut";
        }

        @Override
        public boolean supports(ApplicationType applicationType) {
            return true;
        }

        @Override
        public boolean isVisible() {
            return false;
        }

        @Override
        public int getOrder() {
            return FeaturePhase.BUILD.getOrder() + 1;
        }

        @Override
        public void apply(GeneratorContext generatorContext) {
            Template pyproject = generatorContext.getTemplates().get(PYPROJECT_TEMPLATE);
            if (pyproject == null) {
                return;
            }
            String content = withClientTimeout(render(pyproject));
            generatorContext.addTemplate(PYPROJECT_TEMPLATE, new StringTemplate(pyproject.getPath(), content));
        }

        static String withClientTimeout(String pyproject) {
            if (pyproject.lines().map(String::strip).anyMatch(CLIENT_TIMEOUT_LINE::equals)) {
                return pyproject;
            }
            int table = indexOfLine(pyproject, TEST_RESOURCES_TABLE);
            if (table < 0) {
                String separator = pyproject.isEmpty() || pyproject.endsWith("\n\n") ? "" : pyproject.endsWith("\n") ? "\n" : "\n\n";
                return pyproject + separator + TEST_RESOURCES_TABLE + "\n" + CLIENT_TIMEOUT_LINE + "\n";
            }
            int lineEnd = pyproject.indexOf('\n', table);
            int insertAt = lineEnd < 0 ? pyproject.length() : lineEnd + 1;
            String prefix = lineEnd < 0 ? "\n" : "";
            return pyproject.substring(0, insertAt) + prefix + CLIENT_TIMEOUT_LINE + "\n" + pyproject.substring(insertAt);
        }

        private static int indexOfLine(String content, String line) {
            int from = 0;
            while (from <= content.length()) {
                int index = content.indexOf(line, from);
                if (index < 0) {
                    return -1;
                }
                boolean lineStart = index == 0 || content.charAt(index - 1) == '\n';
                int end = index + line.length();
                boolean lineEnd = end == content.length() || content.charAt(end) == '\n' || content.charAt(end) == '\r';
                if (lineStart && lineEnd) {
                    return index;
                }
                from = index + 1;
            }
            return -1;
        }

        private static String render(Template template) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                template.write(out);
                return out.toString(StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new UncheckedIOException("Could not render " + template.getPath(), e);
            }
        }
    }
}
