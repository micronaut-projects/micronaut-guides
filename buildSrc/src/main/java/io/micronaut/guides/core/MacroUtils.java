package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MacroUtils {
    private MacroUtils() {
    }

    /**
     * Adds include directives to the list of lines for the given source path and tags.
     *
     * @param option        The {@link GuidesOption} containing the build tool and language.
     * @param slug          The slug representing the guide, which will be used as root folder.
     * @param sourcePath    The path to the source file from the guide's project root.
     * @param licenseLoader The {@link LicenseLoader} to calculate the line offset. If null no line offset is added.
     * @param extension     The file extension of the source file.
     * @param indent        The indent value for the include directive. If empty no indent is added.
     * @param tags          The list of tags for the include directive.
     * @return A list of lines with include directives.
     */
    @NonNull
    public static List<String> addIncludes(@NonNull GuidesOption option,
                                           @NonNull LicenseLoader licenseLoader,
                                           @NonNull GuidesConfiguration configuration,
                                           @NonNull String slug,
                                           @NonNull String sourcePath,
                                           @NonNull String extension,
                                           @Nullable String indent,
                                           @NonNull List<String> tags) {
        String sourceDir = getSourceDir(slug, option);
        List<String> lines = new ArrayList<>();
        lines.add("[source," + extension + "]");
        Path path = Path.of(slug, sourceDir, sourcePath);
        lines.add("." + path.normalize().toString().replace(slug+"/"+sourceDir+"/", ""));
        lines.add("----");

        if (!tags.isEmpty()) {
            for (String tag : tags) {
                String attrs = tag;
                if (StringUtils.isNotEmpty(indent)) {
                    attrs += "," + indent;
                }
                lines.add("include::{sourceDir}/" + path + "[" + attrs + "]\n");
            }
        } else {
            List<String> attributes = new ArrayList<>();
            if (fileContainsHeader(extension, configuration) && licenseLoader.getNumberOfLines() > 0) {
                attributes.add("lines=" + licenseLoader.getNumberOfLines() + "..-1");
            }
            if (StringUtils.isNotEmpty(indent)) {
                attributes.add(indent);
            }
            lines.add("include::{sourceDir}/" + path + "[" + String.join(";", attributes) + "]");
        }
        lines.add("----");
        return lines;
    }

    /**
     * Adds include directives to the list of lines for the given source path and tags.
     *
     * @param option        The {@link GuidesOption} containing the build tool and language.
     * @param slug          The slug representing the guide, which will be used as root folder.
     * @param sourcePath    The path to the source file from the guide's project root.
     * @param licenseLoader The {@link LicenseLoader} to calculate the line offset.
     * @param indent        The indent value for the include directive. If empty no indent is added.
     * @param tags          The list of tags for the include directive.
     * @return A list of lines with include directives.
     */
    @NonNull
    public static List<String> addIncludes(@NonNull GuidesOption option,
                                           @NonNull LicenseLoader licenseLoader,
                                           @NonNull GuidesConfiguration configuration,
                                           @NonNull String slug,
                                           @NonNull String sourcePath,
                                           @NonNull String indent,
                                           @NonNull List<String> tags) {
        return addIncludes(option, licenseLoader, configuration, slug, sourcePath, option.getLanguage().toString(), indent, tags);
    }

    @NonNull
    static String getSourceDir(@NonNull String slug, @NonNull GuidesOption option) {
        return slug + "-" + option.getBuildTool() + "-" + option.getLanguage();
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
            return Arrays.stream(attributeValue.split("\\|")).map(it -> "tag=" + it).toList();
        }

        attributeValue = extractTagName(line);
        return attributeValue.isEmpty() ? Collections.emptyList() : Collections.singletonList("tag="+attributeValue);
    }


    @NonNull
    static String pathByFolder(@NonNull GuidesConfiguration guidesConfiguration,
                               @NonNull String appName,
                               @NonNull String fileName,
                               @NonNull String folder,
                               @NonNull GuidesOption option) {
        String module = appName.isEmpty() ? "" : appName + "/";
        return module + "src/" + folder + "/" + option.getLanguage().toString() + "/" + guidesConfiguration.getPackageName().replace(".", "/") + "/" +fileName + "." + option.getLanguage().getExtension();
    }

    @NonNull
    public static String extractIndent(@NonNull String line) {
        String indentValue = extractFromParametersLine(line, "indent");
        return  !indentValue.isEmpty()  ? "indent=" + indentValue : "";
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

    static boolean fileContainsHeader(@NonNull String extension, @NonNull GuidesConfiguration configuration) {
        return configuration.getFilesWithHeader().contains(extension);
    }

    static List<String> findMacroLines(@NonNull String str, @NonNull String macro) {
        return str.lines()
                .filter(line -> line.startsWith(macro+":"))
                .toList();
    }
}
