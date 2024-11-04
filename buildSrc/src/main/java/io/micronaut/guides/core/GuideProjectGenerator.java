package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;

public interface GuideProjectGenerator {
    void generate(@NotNull @NonNull File outputDirectory,
                  @Nullable Guide guide) throws IOException;
}
