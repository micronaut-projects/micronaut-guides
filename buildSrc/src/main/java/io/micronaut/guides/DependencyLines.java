package io.micronaut.guides;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DependencyLines {

    private static final String SCOPE_COMPILE = "compile";
    private static final String SCOPE_IMPLEMENTATION = "implementation";
    private static final String SCOPE_ANNOTATION_PROCESSOR = "annotationProcessor";
    private static final String SCOPE_ANNOTATION_PROCESSOR_KAPT = "kapt";

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

    static String toGradleScope(Map<String, String> attributes, Language language) {
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
            case "annotationProcessor":
                if (language.equals(Language.KOTLIN)) {
                    return "kapt";
                } else if (language.equals(Language.GROOVY)) {
                    return "compileOnly";
                }
            default:
                return s;
        }
    }

    public static List<String> asciidoc(String line, BuildTool buildTool, Language language) {
        return asciidoc(Collections.singletonList(line), buildTool, language);
    }

    public static List<String> asciidoc(List<String> lines, BuildTool buildTool, Language language) {
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
            String gradleScope = Optional.ofNullable(toGradleScope(attributes, language)).orElse(SCOPE_IMPLEMENTATION);
            String mavenScope = Optional.ofNullable(toMavenScope(attributes)).orElse(SCOPE_COMPILE);
            String version = attributes.get("version");
            String callout = extractCallout(attributes);
            boolean pom = "true".equalsIgnoreCase(attributes.getOrDefault("pom", "false"));

            if (buildTool == BuildTool.GRADLE) {
                String rendered = gradleScope;
                if (pom) {
                    rendered += " platform";
                }
                rendered += "(\"" + groupId + ':' + artifactId;
                if (version != null) {
                    rendered += ':' + version;
                }
                rendered += "\")" + callout;
                dependencyLines.add(rendered);
            } else if (buildTool == BuildTool.MAVEN) {
                if (gradleScope.equals(SCOPE_ANNOTATION_PROCESSOR) || gradleScope.equals(SCOPE_ANNOTATION_PROCESSOR_KAPT)) {
                    String mavenScopeAnnotationProcessor = getMavenAnnotationScopeXMLPath(language);

                    dependencyLines.add("<!-- Add the following to your annotationProcessorPaths element -->");
                    dependencyLines.add("<" + mavenScopeAnnotationProcessor + ">" + callout);
                    dependencyLines.add("    <groupId>" + groupId + "</groupId>");
                    dependencyLines.add("    <artifactId>" + artifactId + "</artifactId>");
                    if (StringUtils.isNotEmpty(version)) {
                        dependencyLines.add("    <version>" + version + "</version>");
                    }
                    dependencyLines.add("</" + mavenScopeAnnotationProcessor + ">");
                } else {
                    if (pom) {
                        dependencyLines.add("<!-- Add the following to your dependencyManagement element -->");
                    }
                    dependencyLines.add("<dependency>" + callout);
                    dependencyLines.add("    <groupId>" + groupId + "</groupId>");
                    dependencyLines.add("    <artifactId>" + artifactId + "</artifactId>");
                    if (version != null) {
                        dependencyLines.add("    <version>" + version + "</version>");
                    }
                    if (pom) {
                        dependencyLines.add("    <type>pom</type>");
                        dependencyLines.add("    <scope>import</scope>");
                    } else {
                        dependencyLines.add("    <scope>" + mavenScope + "</scope>");
                    }
                    dependencyLines.add("</dependency>");
                    if (pom) {
                        dependencyLines.add("");
                    }
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

    private static String getMavenAnnotationScopeXMLPath(Language language) {
        switch (language) {
            case JAVA: return "path";
            case KOTLIN: return "annotationProcessorPath";
            default: return ""; // not used for Groovy
        }
    }
}
