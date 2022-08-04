package example.micronaut;

import io.micronaut.core.annotation.NonNull;

public interface Identified {

    @NonNull
    String getId();
}
