package io.micronaut.guides.core;

import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static io.micronaut.guides.core.MacroUtils.*;
import static io.micronaut.guides.core.MacroUtils.addIncludes;

@Singleton
public class RawTestMacroSubstitution implements MacroSubstitution{

    private final GuidesConfiguration guidesConfiguration;
    private final LicenseLoader licenseLoader;

    public RawTestMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        this.guidesConfiguration = guidesConfiguration;
        this.licenseLoader = licenseLoader;
    }

    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        String name = extractName(str, "rawTest");
        String appName = extractAppName(str);

        List<String> tagNames = extractTags(str);
        List<String> tags = CollectionUtils.isEmpty(tagNames)
                ? Collections.emptyList()
                : tagNames.stream().map(it -> "tag=" + it).toList();

        String indent = "";

        String sourcePath = rawTestPath(guidesConfiguration, appName, name, option);

        List<String> lines = addIncludes(option, slug, sourcePath, licenseLoader, option.getTestFramework().toTestFramework().getDefaultLanguage().getExtension(),indent, tags, false);

        return String.join("\n", lines);
    }
}
