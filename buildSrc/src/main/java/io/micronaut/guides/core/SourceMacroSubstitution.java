package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.micronaut.guides.core.MacroUtils.*;

@Singleton
public class SourceMacroSubstitution implements MacroSubstitution {

    private final GuidesConfiguration guidesConfiguration;
    private final LicenseLoader licenseLoader;

    public SourceMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        this.guidesConfiguration = guidesConfiguration;
        this.licenseLoader = licenseLoader;
    }

    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        String sourceDir = getSourceDir(slug, option);
        String name = extractName(str, "source");
        String appName = extractAppName(str);

        List<String> tagNames = extractTags(str);
        List<String> tags = (tagNames != null && !tagNames.isEmpty())
                ? tagNames.stream().map(it -> "tag=" + it).toList()
                : Collections.emptyList();

        String indent = extractIndent(str);

        String sourcePath = mainPath(guidesConfiguration, appName, name, option);
        String normalizedSourcePath = Paths.get(sourcePath).normalize().toString();

        List<String> lines = new ArrayList<>();
        lines.add("[source,"+option.getLanguage().toString()+"]");
        lines.add("." + normalizedSourcePath);
        lines.add("----");

        addIncludes(lines, slug, sourceDir, normalizedSourcePath, licenseLoader, indent, tags);

        return String.join("\n", lines);
    }
}
