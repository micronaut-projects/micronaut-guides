package io.micronaut.guides.core;

import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;
import org.gradle.api.GradleException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class FeaturesMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {

    @Override
    protected String getMacroName() {
        return "features";
    }

    @Override
    protected String getSubstitution(Guide guide, GuidesOption option, String appName) {
        App app = guide.apps().stream()
                .filter(a -> a.name().equals(appName))
                .findFirst()
                .orElse(null);
        List<String> features = app != null ? GuideUtils.getAppVisibleFeatures(app, option.getLanguage()) : new ArrayList<>();
        if (option.getLanguage() == Language.GROOVY) {
            features.remove("graalvm");
        }
        return String.join(",",features);
    }
}
