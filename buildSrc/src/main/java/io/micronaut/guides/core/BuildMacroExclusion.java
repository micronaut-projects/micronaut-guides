package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class BuildMacroExclusion extends MacroExclusion {
    private static final String MACRO_BUILD_EXCLUSION = "exclude-for-build";

    @Override
    protected String getMacroName() {
        return MACRO_BUILD_EXCLUSION;
    }

    @Override
    protected boolean shouldExclude(List<String> params, GuidesOption option, Guide guide) {
        return params.contains(option.getBuildTool().toString());
    }
}
