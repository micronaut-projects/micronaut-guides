package example.micronaut

import io.micronaut.core.annotation.NonNull

interface FruitService {

    Iterable<Fruit> list()

    Fruit save(Fruit fruit)

    Optional<Fruit> find(@NonNull String id)

    Iterable<Fruit> findByNameInList(List<String> name)
}
