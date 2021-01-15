package io.micronaut.guides;

import io.micronaut.starter.options.BuildTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyLines {

    private static final String SCOPE_COMPILE = "compile";
    private static final String SCOPE_IMPLEMENTATION = "implementation";

    static String toMavenScope(Map<String, String> attributes) {
        String s = attributes.get("scope");
        if (s == null) {
            return null;
        }
        switch (s) {
            case "api":
            case "implementation":
                return "compile";
            case "testCompile":
            case "testRuntime":
            case "testRuntimeOnly":
            case "testImplementation":
                return "test";
            case "compileOnly": return "provided";
            case "runtimeOnly": return "runtime";
            default: return s;
        }
    }

    static String toGradleScope(Map<String, String> attributes) {
        String s = attributes.get("scope");
        if (s==null) {
            return null;
        }
        switch (s) {
            case "compile":
                return "implementation";
            case "testCompile":
                return "testImplementation";
            case "test":
                return "testImplementation";
            case "provided":
                return "developmentOnly";
            default: return s;
        }
    }

    public static List<String> asciidoc(String line, BuildTool buildTool) {
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
        String groupId = attributes.containsKey("groupId") ? attributes.get("groupId")  : "io.micronaut";
        String scope = attributes.containsKey("scope") ? attributes.get("scope"): "implementation";
        String gradleScope = toGradleScope(attributes) != null ?  toGradleScope(attributes) : SCOPE_IMPLEMENTATION;
        String mavenScope = toMavenScope(attributes)  != null ? toMavenScope(attributes) : SCOPE_COMPILE;
        List<String> dependencyLines = new ArrayList<>();

        if (buildTool == BuildTool.GRADLE) {

            dependencyLines.add("[source, groovy]");
            dependencyLines.add(".build.gradle");
            dependencyLines.add("----");
            //dependencyLines.add("dependencies {");
            dependencyLines.add(gradleScope + "(\"" + groupId + ":" + artifactId + "\")");
            //dependencyLines.add("}");
            dependencyLines.add("----");
        } else if (buildTool == BuildTool.MAVEN) {
            dependencyLines.add("[source, xml]");
            dependencyLines.add(".pom.xml");
            dependencyLines.add("----");
            //dependencyLines.add("<dependencies>");
            dependencyLines.add("<dependency>");
            dependencyLines.add("    <groupId>" + groupId + "</groupId>");
            dependencyLines.add("     <artifactId>" + artifactId + "</artifactId>");
            if (mavenScope != SCOPE_COMPILE) {
                dependencyLines.add("    <scope>" + mavenScope + "</scope>");
            }
            dependencyLines.add("</dependency>");
            //dependencyLines.add("</dependencies>");
            dependencyLines.add("----");
        }
        return dependencyLines;
    }
}
