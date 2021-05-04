package example.micronaut;

import io.micronaut.core.annotation.NonNull;

public interface ApplicationConfiguration {

    @NonNull
    Integer getMax();
}
