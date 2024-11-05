package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;

public interface FilesTransferUtility {
    void transferFiles(@NotNull @NonNull File outputDirectory,
                       @NotNull @NonNull File inputDirectory,
                       Guide guide) throws IOException;
}
