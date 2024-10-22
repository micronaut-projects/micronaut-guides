package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MacroUtils {
    private MacroUtils() {
    }

    @NonNull
    public static String getSourceDir(@NonNull String slug, GuidesOption option) {
        return slug + "-" + option.getBuildTool() + "-" + option.getLanguage();
    }

    @NonNull
    public static void addIncludes(List<String> lines, String slug, String sourceDir, String sourcePath, LicenseLoader licenseLoader, String indent, List<String> tags) {
        if (!tags.isEmpty()) {
            for (String tag : tags) {
                String attrs = tag;
                if (StringUtils.isNotEmpty(indent)) {
                    attrs += "," + indent;
                }
                lines.add("include::{sourceDir}/" + slug + "/"+sourceDir+"/" + sourcePath + "[" + attrs + "]\n");
            }
        } else {
            List<String> attributes = new ArrayList<>();
            attributes.add("lines=" + licenseLoader.getNumberOfLines() + "..-1");
            if (StringUtils.isNotEmpty(indent)) {
                attributes.add(indent);
            }
            lines.add("include::{sourceDir}/" + slug + "/"+sourceDir+"/" + sourcePath + "[" + String.join(";", attributes) + "]");
        }
        lines.add("----\n");
    }

    @NonNull
    public static String extractName(@NonNull String line, @NonNull String macro) {
        return line.substring(macro.length() + 1, line.indexOf('['));
    }

    @NonNull
    public static String extractAppName(@NonNull  String line) {
        return extractFromParametersLine(line, "app");
    }

    @NonNull
    public static List<String> extractTags(@NonNull  String line) {
        String attributeValue = extractFromParametersLine(line, "tags");
        if (StringUtils.isNotEmpty(attributeValue)) {
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
                                       String folder,
                                       GuidesOption option) {
        String module = !appName.isEmpty() ? appName + "/" : "";
        return module+"src/"+folder+"/"+option.getLanguage().toString()+"/" + guidesConfiguration.getPackageName().replace(".", "/") + "/"+fileName+"."+option.getLanguage().getExtension();
    }

    @NonNull
    public static String extractIndent(@NonNull String line) {
        String indentValue = extractFromParametersLine(line, "indent");
        return  !indentValue.isEmpty()  ? "indent="+indentValue : "";
    }

    @NonNull
    static String extractTagName(@NonNull String line) {
        return extractFromParametersLine(line, "tag");
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
}
