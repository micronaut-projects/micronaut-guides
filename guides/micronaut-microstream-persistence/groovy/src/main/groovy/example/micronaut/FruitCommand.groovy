package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

import javax.validation.constraints.NotEmpty

@Introspected // <1>
class FruitCommand {

    @NonNull
    @NotEmpty // <2>
    final String name

    @Nullable // <3>
    final String description

    FruitCommand(@NonNull String name, @Nullable String description) {
        this.name = name
        this.description = description
    }
}
