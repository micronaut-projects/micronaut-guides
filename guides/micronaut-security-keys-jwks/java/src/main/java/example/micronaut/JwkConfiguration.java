package example.micronaut;

import io.micronaut.core.annotation.NonNull;

public interface JwkConfiguration {
    @NonNull
    String getPrimary();

    @NonNull
    String getSecondary();
}
