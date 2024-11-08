package io.micronaut.guides.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.options.Language;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TestScriptGenerator {

    boolean supportsNativeTest(App app, GuidesOption guidesOption);

    boolean isMicronautFramework(App app);

    boolean supportsNativeTest(Language language);

    String generateNativeTestScript(@NonNull @NotNull List<Guide> metadatas);

    String generateTestScript(@NonNull @NotNull List<Guide> metadatas);

}
