package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.io.File;

public interface AsciidocConverter {

    @NotNull
    @NonNull
    String convert(@NotNull @NonNull File source);
}
