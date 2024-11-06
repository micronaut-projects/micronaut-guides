package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;

import java.io.File;
import java.util.List;

public interface TestScriptGenerator {

    void generateNativeTestScript(@NonNull @NotNull File output,
                                  @NonNull @NotNull List<Guide> metadatas);

    void generateTestScript(@NonNull @NotNull File output,
                            @NonNull @NotNull List<Guide> metadatas);
}
