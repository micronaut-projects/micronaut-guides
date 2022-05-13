package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

interface FruitRepository {

    @NonNull
    Collection<Fruit> list()

    @NonNull
    Fruit create(@NonNull FruitCommand fruit)

    @Nullable
    Fruit update(@NonNull FruitCommand fruit)

    @Nullable
    Fruit find(@NonNull String name)

    void delete(@NonNull FruitCommand fruit)
}
