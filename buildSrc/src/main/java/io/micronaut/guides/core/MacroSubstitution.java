package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.asciidoc.AsciidocMacro;
import io.micronaut.guides.core.asciidoc.Attribute;

public interface MacroSubstitution {
    String APP = "app";
    String APP_NAME_DEFAULT = "default";

    @NonNull
    String substitute(@NonNull String str, @NonNull Guide slug, @NonNull GuidesOption option);

    default App app(Guide guide, AsciidocMacro asciidocMacro) {
        final String appName = appName(asciidocMacro);
        return  guide.apps().stream()
                .filter(a -> a.name().equals(appName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("app not found for app name" + appName));
    }

    default String appName(AsciidocMacro asciidocMacro) {
        return asciidocMacro.attributes().stream()
                .filter(attribute -> attribute.key().equals(APP))
                .map(Attribute::values)
                .filter(l -> !l.isEmpty())
                .map(l -> l.get(0))
                .findFirst()
                .orElse(APP_NAME_DEFAULT);
    }
}
