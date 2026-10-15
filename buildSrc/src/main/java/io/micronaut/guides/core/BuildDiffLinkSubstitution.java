package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.starter.application.ApplicationType;
import jakarta.inject.Singleton;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static io.micronaut.guides.core.MacroUtils.findMacroLines;

@Singleton
public class BuildDiffLinkSubstitution implements MacroSubstitution {
    public static final String MACRO_DIFF_LINK = "diffLink";
    private static final String QUERY_PARAMLANG = "lang";
    private static final String QUERY_PARAM_BUILD = "build";
    private static final String QUERY_PARAM_TEST = "test";
    private static final String QUERY_PARAM_NAME = "name";
    private static final String QUERY_PARAM_TYPE = "type";
    private static final String QUERY_PARAM_PACKAGE = "package";
    private static final String QUERY_PARAM_ACTIVITY = "activity";
    private static final String QUERY_PARAM_FEATURES = "features";
    private static final String ATTRIBUTE_FEATURES = "features";
    private static final String ATTRIBUTE_EXCLUDE_FEATURES = "featureExcludes";
    private final GuidesConfiguration guidesConfiguration;

    public BuildDiffLinkSubstitution(GuidesConfiguration config) {
        this.guidesConfiguration = config;
    }

    private static Set<String> features(App app, AsciidocMacro asciidocMacro, GuidesOption option) {
        Set<String> features = new HashSet<>();
        if (app != null) {
            features.addAll(GuideUtils.getAppVisibleFeatures(app, option.getLanguage()));
        }
        asciidocMacro.attributes().stream()
                .filter(attribute -> attribute.key().equals(ATTRIBUTE_FEATURES))
                .map(Attribute::values)
                .forEach(features::addAll);
        asciidocMacro.attributes().stream()
                .filter(attribute -> attribute.key().equals(ATTRIBUTE_EXCLUDE_FEATURES))
                .map(Attribute::values)
                .forEach(features::removeAll);
        return features;
    }

    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        for (String line : findMacroLines(str, MACRO_DIFF_LINK)) {
            Optional<AsciidocMacro> asciidocMacroOptional = AsciidocMacro.of(MACRO_DIFF_LINK, line);
            if (asciidocMacroOptional.isEmpty()) {
                continue;
            }
            AsciidocMacro asciidocMacro = asciidocMacroOptional.get();
            String res = buildDiffLink(asciidocMacro, guide, option).toString() + "[Diff]";
            str = str.replace(line, res);
        }
        return str;
    }

    private URI buildDiffLink(AsciidocMacro asciidocMacro, Guide guide, GuidesOption option) {
        String appName = appName(asciidocMacro);
        App app = app(guide, asciidocMacro);
        Set<String> features = features(app, asciidocMacro, option);
        UriBuilder uriBuilder = UriBuilder.of(guidesConfiguration.getProjectGeneratorUrl())
                .queryParam(QUERY_PARAMLANG, option.getLanguage().name())
                .queryParam(QUERY_PARAM_BUILD, option.getBuildTool().name())
                .queryParam(QUERY_PARAM_TEST, option.getTestFramework().name())
                .queryParam(QUERY_PARAM_NAME, appName.equals(guidesConfiguration.getDefaultAppName()) ? "micronautguide" : appName)
                .queryParam(QUERY_PARAM_TYPE, app != null ? app.applicationType().name() : ApplicationType.DEFAULT.name())
                .queryParam(QUERY_PARAM_PACKAGE, guidesConfiguration.getPackageName())
                .queryParam(QUERY_PARAM_ACTIVITY, "diff");
        features.forEach(f -> uriBuilder.queryParam(QUERY_PARAM_FEATURES, f));
        return uriBuilder.build();
    }
}
