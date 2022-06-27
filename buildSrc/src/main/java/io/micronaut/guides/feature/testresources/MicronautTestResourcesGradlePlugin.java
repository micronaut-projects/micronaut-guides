package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.template.Writable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MicronautTestResourcesGradlePlugin {
    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        public static final String MICRONAUT_GRADLE_PLUGIN_TEST_RESOURCES_ID = "io.micronaut.test-resources";
        public static final String ARTIFACT_ID = "micronaut-test-resources-plugin";
        public GradlePlugin build() {
            return GradlePlugin.builder()
                    .id(MICRONAUT_GRADLE_PLUGIN_TEST_RESOURCES_ID)
                    .lookupArtifactId(ARTIFACT_ID)
                    .settingsExtension(outputStream -> {
                        String str = "pluginManagement {\n" +
                                "    repositories {\n" +
                                "        mavenLocal()\n" +
                                "        gradlePluginPortal()\n" +
                                "        mavenCentral()\n" +
                                "    }\n" +
                                "}\n";
                        outputStream.write(str.getBytes(StandardCharsets.UTF_8));
                    })
                    .build();
        }
    }
}
