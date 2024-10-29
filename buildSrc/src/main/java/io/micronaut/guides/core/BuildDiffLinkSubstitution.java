package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Singleton;

import static io.micronaut.guides.core.MacroUtils.*;

@Singleton
public class BuildDiffLinkSubstitution implements MacroMetadataSubstitution {

    private final GuidesConfiguration guidesConfiguration;

    public BuildDiffLinkSubstitution(GuidesConfiguration config) {
        this.guidesConfiguration = config;
    }
    @Override
    public String substitute(String str, GuidesOption option, Guide guide) {
        for(String line : findMacroLines(str, "diffLink")){
            String appName;
            if (StringUtils.isNotEmpty(extractAppName(line))) {
                appName = extractAppName(line);
            } else {
                appName = guidesConfiguration.getDefaultAppName();
            }
            App app = guide.apps().stream()
                    .filter(a -> a.name().equals(appName))
                    .findFirst()
                    .orElse(null);

            UriBuilder uriBuilder = UriBuilder.of(guidesConfiguration.getProjectGeneratorUrl())
                    .queryParam("lang", option.getLanguage().name())
                    .queryParam("build", option.getBuildTool().name())
                    .queryParam("test", option.getTestFramework().name())
                    .queryParam("name", appName.equals(guidesConfiguration.getDefaultAppName()) ? "micronautguide" : appName)
                    .queryParam("type", app.applicationType().name())
                    .queryParam("package", guidesConfiguration.getPackageName())
                    .queryParam("activity", "diff");
            featureNames(line, app, option).forEach(f -> uriBuilder.queryParam("features", f));

            String res = "NOTE: If you have an existing Micronaut application and want to add the functionality described here, you can "
                    + uriBuilder.build().toString()
                    + "[view the dependency and configuration changes from the specified features, window=\"_blank\"]"
                    + " and apply those changes to your application.";

            str = str.replace(line,res);
        }
        return str;
    }
}
