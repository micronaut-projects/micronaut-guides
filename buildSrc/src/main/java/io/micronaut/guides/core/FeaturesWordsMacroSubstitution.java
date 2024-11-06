package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FeaturesWordsMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {

    @Override
    protected String getMacroName() {
        return "features-words";
    }

    @Override
    protected String getSubstitution(Guide guide, GuidesOption option, String appName) {
        List<String> features = GuideUtils.getAppVisibleFeatures(app(guide, appName), option.getLanguage())
                .stream()
                .map(feature -> "`" + feature + "`")
                .collect(Collectors.toList());

        if (features.size() > 1) {
            return String.join(", ", features.subList(0, features.size() - 1)) + ", and " + features.get(features.size() - 1);
        }
        return features.get(0);
    }
}
