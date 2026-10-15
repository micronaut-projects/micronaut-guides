package io.micronaut.guides.core;

import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import jakarta.inject.Singleton;

@Singleton
public class FeaturesMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {

    @Override
    protected String getMacroName() {
        return "features";
    }

    @Override
    protected String getSubstitution(Guide guide, GuidesOption option, String appName) {
        return String.join(AsciidocMacro.ATTRIBUTE_SEPARATOR, GuideUtils.getAppVisibleFeatures(app(guide, appName), option.getLanguage()));
    }
}
