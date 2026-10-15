package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TestScriptGenerator {

    boolean supportsNativeTest(@NonNull @NotNull App app, @NonNull @NotNull GuidesOption guidesOption);

    boolean isMicronautFramework(@NonNull @NotNull App app);

    boolean supportsNativeTest(@NonNull @NotNull Language language);

    @NonNull
    @NotNull
    String generateNativeTestScript(@NonNull @NotNull List<Guide> metadatas);

    @NonNull
    @NotNull
    String generateTestScript(@NonNull @NotNull List<Guide> metadatas);

}
