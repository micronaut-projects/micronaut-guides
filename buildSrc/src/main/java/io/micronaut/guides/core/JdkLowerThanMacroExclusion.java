package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class JdkLowerThanMacroExclusion extends MacroExclusion {
    private static final String MACRO_JDK_LOWER_THAN_EXCLUSION = "exclude-for-jdk-lower-than";

    private final GuidesConfiguration guidesConfiguration;

    public JdkLowerThanMacroExclusion(GuidesConfiguration guidesConfiguration) {
        this.guidesConfiguration = guidesConfiguration;
    }

    @Override
    protected String getMacroName() {
        return MACRO_JDK_LOWER_THAN_EXCLUSION;
    }

    @Override
    protected boolean shouldExclude(List<String> params, GuidesOption option, Guide guide) {
        if (StringUtils.isNotEmpty(params.get(0))) {
            Integer minJdk = Integer.valueOf(params.get(0));
            return (guide.minimumJavaVersion() != null ? guide.minimumJavaVersion() : guidesConfiguration.getDefaultMinJdk()) < minJdk;
        }
        return false;
    }
}
