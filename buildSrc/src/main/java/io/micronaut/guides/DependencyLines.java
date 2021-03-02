package io.micronaut.guides;

import io.micronaut.starter.options.BuildTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyLines {

    private static final String SCOPE_COMPILE = "compile";
    private static final String SCOPE_IMPLEMENTATION = "implementation";
    private static final String SCOPE_ANNOTATION_PROCESSOR = "annotationProcessor";

    static String toMavenScope(Map<String, String> attributes) {
        String s = attributes.get("scope");
        if (s == null) {
            return null;
        }
        switch (s) {
            case "api":
            case "implementation":
            case "annotationProcessor":
                return "compile";
            case "testCompile":
            case "testRuntime":
            case "testRuntimeOnly":
            case "testImplementation":
                return "test";
            case "compileOnly":
                return "provided";
            case "runtimeOnly":
                return "runtime";
            default:
                return s;
        }
    }

    static String toGradleScope(Map<String, String> attributes) {
        String s = attributes.get("scope");
        if (s == null) {
            return null;
        }
        switch (s) {
            case "compile":
                return "implementation";
            case "testCompile":
            case "test":
                return "testImplementation";
            case "provided":
                return "developmentOnly";
            default:
                return s;
        }
    }

    public static List<String> asciidoc(String line, BuildTool buildTool) {
        return asciidoc(Collections.singletonList(line), buildTool);
    }

    public static List<String> asciidoc(List<String> lines, BuildTool buildTool) {
        List<String> dependencyLines = new ArrayList<>();

        // Open Asciidoctor code block
        if (buildTool == BuildTool.GRADLE) {
            dependencyLines.add("[source, groovy]");
            dependencyLines.add(".build.gradle");
            dependencyLines.add("----");
        } else if (buildTool == BuildTool.MAVEN) {
            dependencyLines.add("[source, xml]");
            dependencyLines.add(".pom.xml");
            dependencyLines.add("----");
        }

        for (String line : lines) {
            String artifactId = line.substring("dependency:".length(), line.indexOf("["));
            Map<String, String> attributes = new HashMap<>();
            String attributesStr = line.substring(line.indexOf("[") + "[".length(), line.indexOf("]"));
            String[] attrs = attributesStr.split(",");
            for (String att : attrs) {
                String[] keyValues = att.split("=");
                if (keyValues.length == 2) {
                    attributes.put(keyValues[0], keyValues[1]);
                }
            }
            String groupId = attributes.getOrDefault("groupId", "io.micronaut");
            String gradleScope = toGradleScope(attributes) != null ? toGradleScope(attributes) : SCOPE_IMPLEMENTATION;
            String mavenScope = toMavenScope(attributes) != null ? toMavenScope(attributes) : SCOPE_COMPILE;
            String callout = extractCallout(attributes);

            if (buildTool == BuildTool.GRADLE) {
                dependencyLines.add(gradleScope + "(\"" + groupId + ":" + artifactId + "\")" + callout);
            } else if (buildTool == BuildTool.MAVEN) {
                dependencyLines.add("<dependency>" + callout);
                dependencyLines.add("    <groupId>" + groupId + "</groupId>");
                dependencyLines.add("    <artifactId>" + artifactId + "</artifactId>");
                dependencyLines.add("    <scope>" + mavenScope + "</scope>");
                dependencyLines.add("</dependency>");

                if (gradleScope.equals(SCOPE_ANNOTATION_PROCESSOR)) {
                    dependencyLines.add("<!-- Add the following to your annotationProcessorPaths element -->");
                    dependencyLines.add("<path>");
                    dependencyLines.add("    <groupId>" + groupId + "</groupId>");
                    dependencyLines.add("    <artifactId>" + artifactId + "</artifactId>");
                    dependencyLines.add("</path>");
                }
            }
        }

        // Close Asciidoctor code block
        dependencyLines.add("----");

        return dependencyLines;
    }

    private static String extractCallout(Map<String, String> attributes) {
        String callout = attributes.getOrDefault("callout", null);
        return callout != null ? " // <" + callout + ">" : "";
    }
}
