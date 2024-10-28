package io.micronaut.guides.core.macros;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.guides.core.GuidesOption;

public interface MacroSubstitution {
    String APP = "app";

    @NonNull
    String substitute(@NonNull String str, @NonNull String slug, @NonNull GuidesOption option);
}
