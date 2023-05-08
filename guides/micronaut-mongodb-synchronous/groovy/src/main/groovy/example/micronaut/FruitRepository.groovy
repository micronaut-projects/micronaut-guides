package example.micronaut

import io.micronaut.core.annotation.NonNull

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

interface FruitRepository {

    @NonNull
    List<Fruit> list()

    void save(@NonNull @NotNull @Valid Fruit fruit) // <1>
}
