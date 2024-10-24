package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.List;

import static io.micronaut.guides.core.MacroUtils.*;
import static io.micronaut.guides.core.MacroUtils.addIncludes;

@Singleton
public class ResourceMacroSubstitution implements MacroSubstitution {
    private final GuidesConfiguration guidesConfiguration;
    private final LicenseLoader licenseLoader;

    public ResourceMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        this.guidesConfiguration = guidesConfiguration;
        this.licenseLoader = licenseLoader;
    }

    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        for(String line : findMacroLines(str, "resource")){

            String name = extractName(line, "resource");
            String appName = extractAppName(line);
            List<String> tags = extractTags(line);
            String indent = extractIndent(line);
            String sourcePath = resourcePath(appName, name);
            String extension = resolveAsciidoctorLanguage(name);

            List<String> lines = addIncludes(option, licenseLoader, guidesConfiguration, slug, sourcePath, extension, indent, tags);

            str = str.replace(line,String.join("\n", lines));
        }
        return str;
    }

    @NonNull
    private static String resourcePath(@NonNull String appName, @NonNull String fileName) {
        String module = appName.isEmpty() ? "" : appName + "/";
        return module + "src/" + "main/resources/" + fileName;
    }
}
