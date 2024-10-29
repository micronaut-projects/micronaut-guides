package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.starter.application.ApplicationType;
import jakarta.inject.Singleton;

import static io.micronaut.guides.core.MacroUtils.*;

@Singleton
public class BuildDiffLinkSubstitution implements MacroMetadataSubstitution {
    private static final String QUERY_PARAMLANG = "lang";
    private static final String QUERY_PARAM_BUILD = "build";
    private static final String QUERY_PARAM_TEST = "test";
    private static final String QUERY_PARAM_NAME = "name";
    private static final String QUERY_PARAM_TYPE = "type";
    private static final String QUERY_PARAM_PACKAGE = "package";
    private static final String QUERY_PARAM_ACTIVITY = "activity";
    private static final String QUERY_PARAM_FEATURES = "features";
    private final GuidesConfiguration guidesConfiguration;

    public BuildDiffLinkSubstitution(GuidesConfiguration config) {
        this.guidesConfiguration = config;
    }
    @Override
    public String substitute(String str, GuidesOption option, Guide guide) {
        for(String line : findMacroLines(str, "diffLink")) {
            String appName = StringUtils.isNotEmpty(extractAppName(line))
                    ? extractAppName(line)
                    : guidesConfiguration.getDefaultAppName();
            App app = guide.apps().stream()
                    .filter(a -> a.name().equals(appName))
                    .findFirst()
                    .orElse(null);

            UriBuilder uriBuilder = UriBuilder.of(guidesConfiguration.getProjectGeneratorUrl())
                    .queryParam(QUERY_PARAMLANG, option.getLanguage().name())
                    .queryParam(QUERY_PARAM_BUILD, option.getBuildTool().name())
                    .queryParam(QUERY_PARAM_TEST, option.getTestFramework().name())
                    .queryParam(QUERY_PARAM_NAME, appName.equals(guidesConfiguration.getDefaultAppName()) ? "micronautguide" : appName)
                    .queryParam(QUERY_PARAM_TYPE, app != null ? app.applicationType().name() : ApplicationType.DEFAULT.name())
                    .queryParam(QUERY_PARAM_PACKAGE, guidesConfiguration.getPackageName())
                    .queryParam(QUERY_PARAM_ACTIVITY, "diff");
            if (app != null) {
                featureNames(line, app, option).forEach(f -> uriBuilder.queryParam(QUERY_PARAM_FEATURES, f));
            }
            String res = uriBuilder.toString() + "[Diff]";
            str = str.replace(line,res);
        }
        return str;
    }


}
