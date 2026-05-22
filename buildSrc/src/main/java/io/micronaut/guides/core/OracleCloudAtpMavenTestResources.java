package io.micronaut.guides.core;

import io.micronaut.starter.options.BuildTool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class OracleCloudAtpMavenTestResources {
    private static final String FEATURE_ORACLE_CLOUD_ATP = "oracle-cloud-atp";
    private static final String ORACLE_FREE_TEST_RESOURCES_ARTIFACT_ID = "micronaut-test-resources-jdbc-oracle-free";
    private static final String MICRONAUT_MAVEN_PLUGIN_ARTIFACT_ID = "<artifactId>micronaut-maven-plugin</artifactId>";
    private static final String ORACLE_FREE_TEST_RESOURCES_DEPENDENCY = """
          <testResourcesDependencies>
            <dependency>
              <groupId>io.micronaut.testresources</groupId>
              <artifactId>micronaut-test-resources-jdbc-oracle-free</artifactId>
            </dependency>
          </testResourcesDependencies>
""";

    private OracleCloudAtpMavenTestResources() {
    }

    public static void addDependency(Path destinationPath,
                                     List<String> appFeatures,
                                     BuildTool buildTool) throws IOException {
        if (buildTool != BuildTool.MAVEN || !appFeatures.contains(FEATURE_ORACLE_CLOUD_ATP)) {
            return;
        }

        Path pom = destinationPath.resolve("pom.xml");
        String content = Files.readString(pom, StandardCharsets.UTF_8);
        if (content.contains(ORACLE_FREE_TEST_RESOURCES_ARTIFACT_ID)) {
            return;
        }

        int pluginIndex = content.indexOf(MICRONAUT_MAVEN_PLUGIN_ARTIFACT_ID);
        if (pluginIndex < 0) {
            throw new IOException("Could not find micronaut-maven-plugin in " + pom);
        }

        String marker = "        <configuration>\n";
        int markerIndex = content.indexOf(marker, pluginIndex);
        if (markerIndex < 0) {
            throw new IOException("Could not find micronaut-maven-plugin configuration in " + pom);
        }
        String updated = content.substring(0, markerIndex + marker.length())
                + ORACLE_FREE_TEST_RESOURCES_DEPENDENCY
                + content.substring(markerIndex + marker.length());
        Files.writeString(pom, updated, StandardCharsets.UTF_8);
    }
}
