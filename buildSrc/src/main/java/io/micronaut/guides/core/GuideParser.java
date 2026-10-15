package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface GuideParser {
    @NonNull
    List<Guide> parseGuidesMetadata(@NonNull @NotNull File guidesDir,
                                    @NonNull @NotNull String metadataConfigName);

    @NonNull
    Optional<Guide> parseGuideMetadata(@NonNull @NotNull File guidesDir,
                                       @NonNull @NotNull String metadataConfigName);
}
