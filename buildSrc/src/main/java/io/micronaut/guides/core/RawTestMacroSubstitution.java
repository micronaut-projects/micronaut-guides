package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
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
        for(String line : findMacroLines(str, "rawTest")) {

            String name = extractName(line, "rawTest");
            String appName = extractAppName(line);
            List<String> tags = extractTags(line);
            String indent = extractIndent(line);
            String sourcePath = rawTestPath(guidesConfiguration, appName, name, option);

            List<String> lines = addIncludes(option, licenseLoader, guidesConfiguration, slug, sourcePath, option.getTestFramework().toTestFramework().getDefaultLanguage().getExtension(), indent, tags);

            str = str.replace(line,String.join("\n", lines));
        }
        return str;
    }

    @NonNull
    private static String rawTestPath(@NonNull GuidesConfiguration guidesConfiguration,
                              @NonNull String appName,
                              @NonNull String name,
                              GuidesOption option) {
        String module = appName.isEmpty() ? "" : appName + "/";
        String fileExtension = option.getTestFramework().toTestFramework().getDefaultLanguage().getExtension();
        String langTestFolder = option.getTestFramework().toTestFramework().getDefaultLanguage().getTestSrcDir();
        return module+langTestFolder + "/" + guidesConfiguration.getPackageName().replace(".", "/") + "/" + name + "." + fileExtension;
    }
}
