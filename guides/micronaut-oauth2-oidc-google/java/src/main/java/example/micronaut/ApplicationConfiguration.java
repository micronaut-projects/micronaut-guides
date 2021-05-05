package example.micronaut;

import io.micronaut.core.annotation.NonNull;

public interface ApplicationConfiguration {

    @NonNull
    String getHostedDomain();
}
