package example.micronaut

import io.micronaut.core.annotation.NonNull

interface ApplicationConfiguration {

    @NonNull
    String getHostedDomain()
}
