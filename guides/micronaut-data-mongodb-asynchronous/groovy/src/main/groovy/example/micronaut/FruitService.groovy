package example.micronaut

import io.micronaut.core.annotation.NonNull
import org.reactivestreams.Publisher

interface FruitService {

    Publisher<Fruit> list()

    Publisher<Fruit> save(Fruit fruit)

    Publisher<Fruit> find(@NonNull String id)

    Publisher<Fruit> findByNameInList(List<String> name)
}
