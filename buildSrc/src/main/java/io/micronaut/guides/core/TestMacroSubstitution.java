package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

import static io.micronaut.guides.core.MacroUtils.*;
import static io.micronaut.starter.api.TestFramework.SPOCK;

@Singleton
public class TestMacroSubstitution implements MacroSubstitution{

    private final GuidesConfiguration guidesConfiguration;
    private final LicenseLoader licenseLoader;

    public TestMacroSubstitution(GuidesConfiguration guidesConfiguration, LicenseLoader licenseLoader) {
        this.guidesConfiguration = guidesConfiguration;
        this.licenseLoader = licenseLoader;
    }

    @Override
    public String substitute(String str, String slug, GuidesOption option) {
        for(String line : findMacroLines(str, "test")){

            String name = extractName(line, "test");
            String appName = extractAppName(line);
            List<String> tags = extractTags(line);
            String indent = extractIndent(line);
            String sourcePath = testPath(guidesConfiguration, appName, name, option);

            List<String> lines = addIncludes(option, licenseLoader, guidesConfiguration, slug, sourcePath, indent, tags);

            str = str.replace(line,String.join("\n", lines));
        }
        return str;
    }

    @NonNull
    private static String testPath(@NonNull GuidesConfiguration guidesConfiguration,
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
}
