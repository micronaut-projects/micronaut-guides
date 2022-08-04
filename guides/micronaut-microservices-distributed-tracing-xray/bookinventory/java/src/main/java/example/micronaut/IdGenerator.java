package example.micronaut;

import io.micronaut.core.annotation.NonNull;

@FunctionalInterface
public interface IdGenerator {

    @NonNull
    String generate();
}
