package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
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
        for(String line : findMacroLines(str, "source")){

            String name = extractName(line, "source");
            String appName = extractAppName(line);
            List<String> tags = extractTags(line);
            String indent = extractIndent(line);
            String sourcePath = mainPath(guidesConfiguration, appName, name, option);

            List<String> lines = addIncludes(option, licenseLoader, guidesConfiguration, slug, sourcePath, indent, tags);

            str = str.replace(line,String.join("\n", lines));
        }
        return str;
    }

    @NonNull
    private static String mainPath(@NonNull GuidesConfiguration guidesConfiguration,
                                  @NonNull String appName,
                                  @NonNull String fileName,
                                  @NonNull GuidesOption option) {
        return pathByFolder(guidesConfiguration, appName, fileName, "main", option);
    }
}
