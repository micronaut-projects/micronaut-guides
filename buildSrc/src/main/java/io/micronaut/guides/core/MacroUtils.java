package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class MacroUtils {
    private MacroUtils() {
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
