package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.File;

public interface AsciidocConverter {

    void convert(@NotNull @NonNull File source, @NotNull @NonNull File destination);

    @Nullable
    String convert(@NotNull @NonNull File source);
}
