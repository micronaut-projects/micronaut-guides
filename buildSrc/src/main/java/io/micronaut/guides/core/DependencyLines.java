package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

import java.util.*;

import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.BuildTool.MAVEN;
import static io.micronaut.starter.options.BuildTool.PYRONAUT;
import static io.micronaut.starter.options.Language.GROOVY;
import static io.micronaut.starter.options.Language.KOTLIN;

public class DependencyLines {

    private static final String SCOPE_COMPILE = "compile";
    private static final String SCOPE_IMPLEMENTATION = "implementation";
    private static final String SCOPE_ANNOTATION_PROCESSOR = "annotationProcessor";
    private static final String SCOPE_ANNOTATION_PROCESSOR_KAPT = "kapt";

    private static String toMavenScope(Map<String, String> attributes) {
        String s = attributes.get("scope");
        if (s == null) {
            return null;
        }
        return switch (s) {
            case "api", "implementation", "annotationProcessor" -> "compile";
            case "testCompile", "testRuntimeOnly", "testImplementation" -> "test";
            case "compileOnly" -> "provided";
            case "runtimeOnly" -> "runtime";
            default -> s;
        };
    }

    private static String toGradleScope(Map<String, String> attributes, Language language) {
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
                if (language == KOTLIN) {
                    return "kapt";
                }
                if (language == GROOVY) {
                    return "compileOnly";
                }
            default:
                return s;
        }
    }

    private static String toPyronautScope(Map<String, String> attributes) {
        String s = attributes.get("scope");
        if (s == null) {
            return "runtime";
        }
        return switch (s) {
            case "annotationProcessor", "compileOnly" -> "build";
            case "testCompile", "test", "testRuntimeOnly", "testImplementation", "testCompileOnly", "testAnnotationProcessor" -> "test";
            default -> "runtime";
        };
    }

    public static List<String> asciidoc(String line, BuildTool buildTool, Language language) {
        return asciidoc(Collections.singletonList(line), buildTool, language);
    }

    public static List<String> asciidoc(List<String> lines, BuildTool buildTool, Language language) {
        if (buildTool == PYRONAUT) {
            return pyronautAsciidoc(lines);
        }
        List<String> dependencyLines = new ArrayList<>();

        // Open Asciidoctor code block
        if (buildTool == GRADLE) {
            dependencyLines.add("[source, groovy]");
            dependencyLines.add(".build.gradle");
            dependencyLines.add("----");
        } else if (buildTool == MAVEN) {
            dependencyLines.add("[source, xml]");
            dependencyLines.add(".pom.xml");
            dependencyLines.add("----");
        } else if (buildTool == PYRONAUT) {
            dependencyLines.add("[source, toml]");
            dependencyLines.add(".pyproject.toml");
            dependencyLines.add("----");
            dependencyLines.add("[tool.pyronaut.dependencies]");
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
            String versionProperty = attributes.get("versionProperty");
            String callout = extractCallout(attributes);
            boolean pom = "true".equalsIgnoreCase(attributes.getOrDefault("pom", "false"));

            if (buildTool == GRADLE) {
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
            } else if (buildTool == MAVEN) {
                if (gradleScope.equals(SCOPE_ANNOTATION_PROCESSOR) || gradleScope.equals(SCOPE_ANNOTATION_PROCESSOR_KAPT)) {
                    String mavenScopeAnnotationProcessor = getMavenAnnotationScopeXMLPath(language);

                    dependencyLines.add("<!-- Add the following to your annotationProcessorPaths element -->");
                    dependencyLines.add("<" + mavenScopeAnnotationProcessor + ">" + callout);
                    dependencyLines.add("    <groupId>" + groupId + "</groupId>");
                    dependencyLines.add("    <artifactId>" + artifactId + "</artifactId>");
                    if (StringUtils.isNotEmpty(version)) {
                        dependencyLines.add("    <version>" + version + "</version>");
                    } else if (StringUtils.isNotEmpty(versionProperty)) {
                        dependencyLines.add("    <version>" + versionProperty + "</version>");
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
            } else if (buildTool == PYRONAUT) {
                String coordinate = groupId + ':' + artifactId;
                if (version != null) {
                    coordinate += ':' + version;
                }
                String tomlCallout = callout.isEmpty() ? "" : " #" + callout.substring(" //".length());
                dependencyLines.add(toPyronautScope(attributes) + " = [\"" + coordinate + "\"]" + tomlCallout);
            }
        }

        // Close Asciidoctor code block
        dependencyLines.add("----");

        return dependencyLines;
    }

    private static List<String> pyronautAsciidoc(List<String> lines) {
        Map<String, List<String>> dependencies = new LinkedHashMap<>();
        dependencies.put("runtime", new ArrayList<>());
        dependencies.put("build", new ArrayList<>());
        dependencies.put("test", new ArrayList<>());

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
            String version = attributes.get("version");
            String coordinate = groupId + ':' + artifactId + (version != null ? ':' + version : "");
            String callout = extractCallout(attributes);
            String tomlCallout = callout.isEmpty() ? "" : " #" + callout.substring(" //".length());
            dependencies.get(toPyronautScope(attributes)).add("\"" + coordinate + "\"," + tomlCallout);
        }

        List<String> dependencyLines = new ArrayList<>();
        dependencyLines.add("[source, toml]");
        dependencyLines.add(".pyproject.toml");
        dependencyLines.add("----");
        dependencyLines.add("[tool.pyronaut.dependencies]");
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            dependencyLines.add(entry.getKey() + " = [");
            entry.getValue().forEach(value -> dependencyLines.add("    " + value));
            dependencyLines.add("]");
        }
        dependencyLines.add("----");
        return dependencyLines;
    }

    private static String extractCallout(Map<String, String> attributes) {
        String callout = attributes.getOrDefault("callout", null);
        return callout != null ? " // <" + callout + ">" : "";
    }

    private static String getMavenAnnotationScopeXMLPath(Language language) {
        return switch (language) {
            case JAVA -> "path";
            case KOTLIN -> "annotationProcessorPath";
            default -> ""; // not used for Groovy
        };
    }
}
