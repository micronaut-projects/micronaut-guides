package example.micronaut

import io.micronaut.core.annotation.NonNull

interface JwkConfiguration {

    @NonNull
    String getPrimary()

    @NonNull
    String getSecondary()
}
