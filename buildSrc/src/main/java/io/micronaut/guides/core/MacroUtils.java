package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.options.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public final class MacroUtils {
    private MacroUtils() {
    }

    @NonNull
    public static String extractAppName(@NonNull  String line) {
        return extractFromParametersLine(line, "app");
    }

    @NonNull
    static String extractFromParametersLine(@NonNull String line, @NonNull String attributeName) {
        String[] attrs = line.substring(line.indexOf("[") + 1, line.indexOf("]")).split(",");
        return Arrays.stream(attrs)
                .filter(attr -> attr.startsWith(attributeName))
                .map(attr -> attr.split("="))
                .map(parts -> parts[1])
                .findFirst()
                .orElse("");
    }

    @NonNull
    static List<String> featureNames(@NonNull String line,
                                     @NonNull App app,
                                     @NonNull GuidesOption guidesOption) {
        String features = extractFromParametersLine(line, "features");
        List<String> featureNames;
        if (StringUtils.isNotEmpty(features)) {
            featureNames = Arrays.asList(features.split("\\|"));
        } else {
            featureNames = new ArrayList<>(GuideUtils.getAppVisibleFeatures(app, guidesOption.getLanguage()));
        }

        String featureExcludes = extractFromParametersLine(line, "featureExcludes");
        List<String> excludedFeatureNames;
        if (featureExcludes != null && !featureExcludes.isEmpty()) {
            excludedFeatureNames = Arrays.asList(featureExcludes.split("\\|"));
        } else {
            excludedFeatureNames = new ArrayList<>();
        }
        featureNames.removeAll(excludedFeatureNames);

        if (guidesOption.getLanguage() == Language.GROOVY) {
            featureNames.remove("graalvm");
        }
        return featureNames;
    }


    @NonNull
    static String getSourceDir(@NonNull String slug, @NonNull GuidesOption option) {
        return slug + "-" + option.getBuildTool() + "-" + option.getLanguage();
    }

    static List<String> findMacroLines(@NonNull String str, @NonNull String macro) {
        return str.lines()
                .filter(line -> line.startsWith(macro+":"))
                .toList();
    }

    static List<String> findMacroGroups(@NonNull String str, @NonNull String macro) {
        List<String> matches = new ArrayList<>();
        String pattern = ":"+macro+":";
        int startIndex = 0;

        while (true) {
            startIndex = str.indexOf(pattern, startIndex);
            if (startIndex == -1) break;

            int endIndex = str.indexOf(pattern, startIndex + pattern.length());
            if (endIndex == -1) break;

            String match = str.substring(startIndex, endIndex + pattern.length());
            matches.add(match);

            startIndex = endIndex + pattern.length();
        }

        return matches;
    }

    static List<String> findMacroGroupsNested(@NonNull String str, @NonNull String macro) {
        List<String> matches = new ArrayList<>();
        String pattern = ":"+macro+":";

        List<String> lines = str.lines().toList();
        Stack<Integer> stack = new Stack<>();

        for (int i=0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (isGroupStart(line, pattern)) {
                stack.push(i);
            } else if (isGroupEnd(line, pattern)) {
                if (!stack.isEmpty()) {
                    int start = stack.pop();
                    matches.add(String.join("\n", lines.subList(start, i+1)));
                } else{
                    throw new UnsupportedOperationException("Unbalanced macro group");
                }
            }
        }

        return matches;
    }

    private static boolean isGroupStart(String line, String macro) {
        return line.matches(macro+"[a-zA-Z,]+");
    }

    private static boolean isGroupEnd(String line, String macro) {
        return line.matches(macro+"(?![a-zA-Z,]+$)");
    }

    @NonNull
    static String resolveAsciidoctorLanguage(@NonNull String extension) {
        return switch (extension.toLowerCase()) {
            case "yml", "yaml" -> "yaml";
            case "html", "vm", "hbs" -> "html";
            case "xml" -> "xml";
            default -> extension.toLowerCase();
        };
    }
}
