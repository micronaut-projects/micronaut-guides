package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.micronaut.guides.core.MacroUtils.*;

@Singleton
public class SourceMacroSubstitution implements MacroSubstitution {

    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        String sourceDir = slug + "-" + option.getBuildTool() + "-" + option.getLanguage();;
        String name = extractName(str, "source");
        String appName = extractAppName(str);

        List<String> tagNames = extractTags(str);
        List<String> tags = (tagNames != null && !tagNames.isEmpty())
                ? tagNames.stream().map(it -> "tag=" + it).collect(Collectors.toList())
                : Collections.emptyList();

        String indent = extractIndent(str);

        String sourcePath = mainPath(appName, name, option);
        String normalizedSourcePath = Paths.get(sourcePath).normalize().toString();

        List<String> lines = new ArrayList<>();
        lines.add("[source,"+option.getLanguage().toString()+"]");
        lines.add("." + normalizedSourcePath);
        lines.add("----");

        if (tags != null && !tags.isEmpty()) {
            for (String tag : tags) {
                String attrs = tag;
                if (StringUtils.isNotEmpty(indent)) {
                    attrs += "," + indent;
                }
                lines.add("include::{sourceDir}/" + slug + "/"+sourceDir+"/" + sourcePath + "[" + attrs + "]\n");
            }
        } else {
            List<String> attributes = new ArrayList<>();
            attributes.add("lines=" + MacroUtils.numberOfLinesInLicenseHeader() + "..-1");
            if (StringUtils.isNotEmpty(indent)) {
                attributes.add(indent);
            }
            lines.add("include::{sourceDir}/" + slug + "/"+sourceDir+"/" + sourcePath + "[" + String.join(";", attributes) + "]");
        }

        lines.add("----\n");

        return lines.stream().collect(Collectors.joining("\n"));
    }
}
