package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;

public interface WebsiteGenerator {

    void generate(@NotNull @NonNull File guidesInputDirectory,
                  @NotNull @NonNull File outputDirectory,
                  @Nullable String slug) throws IOException;
}
