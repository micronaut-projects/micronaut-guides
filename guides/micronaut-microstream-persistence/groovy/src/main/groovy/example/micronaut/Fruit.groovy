package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import javax.validation.constraints.NotBlank

@Introspected // <1>
class Fruit {

    @NonNull
    @NotBlank // <2>
    final String name

    @Nullable // <3>
    String description

    Fruit(@NonNull String name) {
        this(name, null)
    }

    @Creator
    Fruit(@NonNull String name, @Nullable String description) {
        this.name = name
        this.description = description
    }
}
