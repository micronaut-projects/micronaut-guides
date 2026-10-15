package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;

public interface GuideProjectZipper {

    void zipDirectory(@NonNull @NotNull String sourceDir, @NonNull @NotNull String outputFile) throws IOException;
}
