package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;

import java.util.stream.Collectors;

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

            String link = "https://micronaut.io/launch?" +
                    featureNames(line, app, option).stream()
                            .map(f -> "features=" + f)
                            .collect(Collectors.joining("&")) +
                    "&lang=" + option.getLanguage().name() +
                    "&build=" + option.getBuildTool().name() +
                    "&test=" + option.getTestFramework().name() +
                    "&name=" + (appName.equals(guidesConfiguration.getDefaultAppName()) ? "micronautguide" : appName) +
                    "&type=" + app.applicationType().name() +
                    "&package=example.micronaut" +
                    "&activity=diff" +
                    "[view the dependency and configuration changes from the specified features, window=\"_blank\"]";

            String res = "NOTE: If you have an existing Micronaut application and want to add the functionality described here, you can " +
                    link + " and apply those changes to your application.";

            str = str.replace(line,res);
        }
        return str;
    }
}
