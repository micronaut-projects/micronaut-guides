package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.File;

public interface ProjectGenerator {

    void generate(@NotNull @NonNull File outputDirectory,
                  @Nullable Guide guide);
}
