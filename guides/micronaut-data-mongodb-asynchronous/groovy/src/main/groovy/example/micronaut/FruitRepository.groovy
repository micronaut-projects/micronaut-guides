package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.mongodb.annotation.MongoRepository
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository
import org.reactivestreams.Publisher

@MongoRepository // <1>
interface FruitRepository extends ReactiveStreamsCrudRepository<Fruit, String> { // <2>

    @NonNull
    Publisher<Fruit> findByNameInList(@NonNull List<String> names) // <3>
}
