package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class BuildMacroExclusion extends MacroExclusion {
    @Override
    protected String getMacroName() {
        return "exclude-for-build";
    }

    @Override
    protected boolean shouldExclude(List<String> params, GuidesOption option, Guide guide) {
        return params.contains(option.getBuildTool().toString());
    }
}
