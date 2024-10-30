package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MacroUtils {
    private MacroUtils() {
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
    static String getSourceDir(@NonNull String slug, @NonNull GuidesOption option) {
        return slug + "-" + option.getBuildTool() + "-" + option.getLanguage();
    }

    static List<String> findMacroLines(@NonNull String str, @NonNull String macro) {
        return str.lines()
                .filter(line -> line.startsWith(macro+":"))
                .toList();
    }

    static List<String> findMacroInstances(@NonNull String str, @NonNull String macro) {
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile("@(?:([\\w-]*):)?"+macro+"@");
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
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

    @NonNull
    static List<String> extractMacroGroupParameters(@NonNull String line, @NonNull String macro) {
        return Arrays.stream(line.substring(macro.length() + 2).split(",")).filter(el -> !el.isEmpty()).toList();
    }

    @NonNull
    static List<List<String>> findMacroGroupsNested(@NonNull String str, @NonNull String macro) {
        List<List<String>> matches = new ArrayList<>();
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
                    matches.add(lines.subList(start, i+1));
                } else{
                    throw new UnsupportedOperationException("Unbalanced macro group");
                }
            }
        }

        return matches;
    }

    private static boolean isGroupStart(@NonNull String line, @NonNull String macro) {
        return line.matches(macro+"[a-zA-Z0-9,]+");
    }

    private static boolean isGroupEnd(@NonNull String line, @NonNull String macro) {
        return line.matches(macro+"(?![a-zA-Z0-9,]+$)");
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
