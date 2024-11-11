package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.io.IOException;

public interface GuideProjectZipper {

    void zipDirectory(@NonNull @NotNull File folderToZip, @NonNull @NotNull File zipFile) throws IOException;
}
