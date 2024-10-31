package io.micronaut.guides.core;

import jakarta.inject.Singleton;

@Singleton
public class FeaturesMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {

    @Override
    protected String getMacroName() {
        return "features";
    }

    @Override
    protected String getSubstitution(Guide guide, GuidesOption option, String appName) {
        return String.join(",",MacroUtils.featuresForApp(guide,option,appName));
    }
}
