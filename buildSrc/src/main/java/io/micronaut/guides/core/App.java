package io.micronaut.guides.core;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record App(@Nullable String packageName) {
}
