package io.micronaut.guides.core.asciidoc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;

import java.util.Optional;

public record Argument(String key, String value) {
    private static final String ARGUMENT_DELIMITATOR = ":";

    @NonNull
    public static Optional<Argument> of(@NonNull String str) {
        if (StringUtils.isEmpty(str)) {
            return Optional.empty();
        }

        String[] parts = str.split("=", 2);
        if (parts.length == 2 && !parts[0].isBlank() && !parts[1].isBlank()) {
            return Optional.of(new Argument(parts[0], parts[1]));
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return ARGUMENT_DELIMITATOR + key + ARGUMENT_DELIMITATOR + " " + value;
    }
}

