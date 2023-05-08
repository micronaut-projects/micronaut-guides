package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
class Fruit {

    @NonNull
    @NotBlank // <2>
    final String name

    @Nullable // <3>
    String description

    Fruit(@NonNull String name, @Nullable String description) {
        this.name = name
        this.description = description
    }
}
