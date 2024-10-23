package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.micronaut.starter.api.TestFramework.SPOCK;

public final class MacroUtils {
    private MacroUtils() {
    }

    @NonNull
    public static String getSourceDir(@NonNull String slug, @NonNull GuidesOption option) {
        return slug + "-" + option.getBuildTool() + "-" + option.getLanguage();
    }

    @NonNull
    public static List<String> addIncludes(@NonNull GuidesOption option,
                                           @NonNull String slug,
                                           @NonNull String sourcePath,
                                           @NonNull LicenseLoader licenseLoader,
                                           String indent,
                                           @NonNull List<String> tags) {
        String sourceDir = getSourceDir(slug, option);
        List<String> lines = new ArrayList<>();
        lines.add("[source," + option.getLanguage().toString() + "]");
        String normalizedSourcePath = Paths.get(sourcePath).normalize().toString();
        lines.add("." + normalizedSourcePath);
        lines.add("----");

        if (!tags.isEmpty()) {
            for (String tag : tags) {
                String attrs = tag;
                if (StringUtils.isNotEmpty(indent)) {
                    attrs += "," + indent;
                }
                lines.add("include::{sourceDir}/" + slug + "/" + sourceDir + "/" + sourcePath + "[" + attrs + "]\n");
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
        return lines;
    }

    @NonNull
    public static List<String> addIncludesResources(@NonNull String str, @NonNull String slug, @NonNull GuidesOption option, @NonNull String resourceDir, @NonNull String macro) {
        String name = extractName(str, macro);
        String appName = extractAppName(str);

        List<String> tagNames = extractTags(str);
        List<String> tags = CollectionUtils.isEmpty(tagNames)
                ? Collections.emptyList()
                : tagNames.stream().map(it -> "tag=" + it).toList();

        String sourceDir = getSourceDir(slug, option);
        String asciidoctorLang = resolveAsciidoctorLanguage(name);
        String module = appName.isEmpty() ? "" : appName + "/";

        List<String> lines = new ArrayList<>();
        String pathcallout = name.startsWith("../") ?
                "." + module + "src/" + resourceDir + "/" + name.substring("../".length()) :
                "." + module + "src/" + resourceDir + "/resources/" + name;

        lines.add("[source," + asciidoctorLang + "]");
        lines.add(pathcallout);
        lines.add("----");

        if (!tags.isEmpty()) {
            for (String tag : tags) {
                lines.add("include::{sourceDir}/" + slug + "/"+sourceDir+"/" + module + "src/" + resourceDir + "/resources/" + name + "[" + tag + "]\n");
            }
        } else {
            lines.add("include::{sourceDir}/" + slug + "/"+sourceDir+"/" + module + "src/" + resourceDir + "/resources/" + name + "[]");
        }
        lines.add("----\n");
        return lines;
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
                                  @NonNull String fileName,
                                  GuidesOption option) {
        return pathByFolder(guidesConfiguration, appName, fileName, "main", option);
    }

    @NonNull
    static String testPath(@NonNull GuidesConfiguration guidesConfiguration,
                           @NonNull String appName,
                           @NonNull String name,
                           GuidesOption option) {
        String fileName = name;

        if (name.endsWith("Test")) {
            fileName = name.substring(0, name.indexOf("Test"));
            fileName += option.getTestFramework() == SPOCK ? "Spec" : "Test";
        }

        return pathByFolder(guidesConfiguration, appName, fileName, "test", option);
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

    @NonNull
    static String resolveAsciidoctorLanguage(@NonNull String fileName) {
        String extension = fileName.indexOf(".") > 0 ?
                fileName.substring(fileName.lastIndexOf(".") + 1) : "";

        return switch (extension.toLowerCase()) {
            case "yml", "yaml" -> "yaml";
            case "html", "vm", "hbs" -> "html";
            case "xml" -> "xml";
            default -> extension.toLowerCase();
        };
    }
}
