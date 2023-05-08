package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

import jakarta.validation.constraints.NotBlank

@MappedEntity // <1>
class Fruit {

    @Id // <2>
    @GeneratedValue
    String id

    @NonNull
    @NotBlank // <3>
    final String name

    @Nullable
    String description

    Fruit(@NonNull String name, @Nullable String description) {
        this.name = name
        this.description = description
    }
}
