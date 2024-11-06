package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;

public interface GuideProjectGenerator {
    void generate(@NotNull @NonNull File outputDirectory,
                  @NotNull @NonNull Guide guide) throws IOException;
}
