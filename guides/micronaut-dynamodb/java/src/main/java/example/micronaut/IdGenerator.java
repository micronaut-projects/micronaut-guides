package example.micronaut;

import io.micronaut.core.annotation.NonNull;

@FunctionalInterface // <1>
public interface IdGenerator {

    @NonNull
    String generate();
}
