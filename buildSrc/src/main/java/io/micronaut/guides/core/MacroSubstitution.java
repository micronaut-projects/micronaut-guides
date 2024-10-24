package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;

public interface MacroSubstitution {
    @NonNull
    String substitute(@NonNull String str, @NonNull String slug, @NonNull GuidesOption option);
}
