package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MacroUtils {
    private MacroUtils() {
    }

    public static String extractName(String line, String macro) {
        return line.substring(macro.length()+1, line.indexOf('['));
    }

    public static String extractAppName(String line) {
        return extractFromParametersLine(line, "app");
    }

    public static List<String> extractTags(String line) {
        String attributeValue = extractFromParametersLine(line, "tags");
        if (attributeValue != null && !attributeValue.isEmpty()) {
            return Arrays.asList(attributeValue.split("\\|"));
        }

        return extractTagName(line).isEmpty() ? Collections.emptyList() : Collections.singletonList(extractTagName(line));
    }

    @NonNull
    public static String mainPath(@NonNull GuidesConfiguration guidesConfiguration,
                                  @NonNull String appName,
                           @NonNull String fileName, GuidesOption option) {
        return pathByFolder(guidesConfiguration, appName, fileName, "main", option);
    }

    @NonNull
    private static String pathByFolder(@NonNull GuidesConfiguration guidesConfiguration,
                                       @NonNull String appName,
                                       @NonNull String fileName,
                                       String folder, GuidesOption option) {
        String module = appName != "" ? appName + "/" : "";
        return module+"src/"+folder+"/"+option.getLanguage().toString()+"/" + guidesConfiguration.getPackageName().replaceAll("\\.", "/") + "/"+fileName+"."+option.getLanguage().getExtension();
    }

    public static String extractIndent(String line) {
        String indentValue = extractFromParametersLine(line, "indent");
        return indentValue != "" ? "indent="+indentValue : "";
    }

    static String extractTagName(String line) {
        return extractFromParametersLine(line, "tag");
    }

    static String extractFromParametersLine(String line, String attributeName) {
        String[] attrs = line.substring(line.indexOf("[") + 1, line.indexOf("]")).split(",");

        return Arrays.stream(attrs)
                .filter(attr -> attr.startsWith(attributeName))
                .map(attr -> attr.split("="))
                .map(parts -> parts[1])
                .findFirst()
                .orElse("");
    }
}
