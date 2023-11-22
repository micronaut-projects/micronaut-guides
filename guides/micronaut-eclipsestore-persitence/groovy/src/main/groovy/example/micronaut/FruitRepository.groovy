package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

interface FruitRepository {

    @NonNull
    Collection<Fruit> list()

    @NonNull
    Fruit create(@NonNull @NotNull @Valid FruitCommand fruit) // <1>
            throws FruitDuplicateException

    @Nullable
    Fruit update(@NonNull @NotNull @Valid FruitCommand fruit) // <1>

    @Nullable
    Fruit find(@NonNull @NotBlank String name)

    void delete(@NonNull @NotNull @Valid FruitCommand fruit) // <1>
}
