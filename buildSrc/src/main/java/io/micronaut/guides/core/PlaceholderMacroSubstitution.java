package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;

import static io.micronaut.starter.api.TestFramework.SPOCK;

public class PlaceholderMacroSubstitution implements MacroSubstitution {

    private GuidesConfiguration guidesConfiguration;

    public PlaceholderMacroSubstitution(GuidesConfiguration guidesConfiguration){
        this.guidesConfiguration = guidesConfiguration;
    }
    @Override
    public String substitute(String str, Guide guide, GuidesOption option) {
        str = str.replace("{githubSlug}", guide.slug());
        str = str.replace("@language@", StringUtils.capitalize(option.getLanguage().toString()));
        str = str.replace("@guideTitle@", guide.title());
        str = str.replace("@guideIntro@", guide.intro());
        str = str.replace("@micronaut@", "version");
        str = str.replace("@lang@", option.getLanguage().toString());
        str = str.replace("@build@", option.getBuildTool().toString());
        str = str.replace("@testFramework@", option.getTestFramework().toString());
        str = str.replace("@authors@", String.join(", ", guide.authors()));
        str = str.replace("@languageextension@", option.getLanguage().getExtension());
        str = str.replace("@testsuffix@", option.getTestFramework() == SPOCK ? "Spec" : "Test");
        str = str.replace("@sourceDir@", "projectName");
        str = str.replace("@minJdk@", guide.minimumJavaVersion() != null ? guide.minimumJavaVersion() : guidesConfiguration.get);
        str = str.replace("@api@", "https://docs.micronaut.io/latest/api");
    }
}
