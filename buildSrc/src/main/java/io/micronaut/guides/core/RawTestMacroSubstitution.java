package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static io.micronaut.guides.core.MacroUtils.*;
import static io.micronaut.guides.core.MacroUtils.addIncludes;

@Singleton
public class RawTestMacroSubstitution implements MacroSubstitution{

    private final GuidesConfiguration guidesConfiguration;

    public RawTestMacroSubstitution(GuidesConfiguration guidesConfiguration) {
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        String name = extractName(str, "rawTest");
        String appName = extractAppName(str);

        List<String> tagNames = extractTags(str);
        List<String> tags = (tagNames != null && !tagNames.isEmpty())
                ? tagNames.stream().map(it -> "tag=" + it).toList()
                : Collections.emptyList();

        String indent = "";

        String sourcePath = rawTestPath(guidesConfiguration, appName, name, option);

        List<String> lines = addIncludes(option, slug, sourcePath, null, option.getTestFramework().toTestFramework().getDefaultLanguage().getExtension(),indent, tags);

        return String.join("\n", lines);
    }
}
