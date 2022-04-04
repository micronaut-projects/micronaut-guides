package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.mongodb.annotation.MongoRepository
import io.micronaut.data.repository.CrudRepository

@MongoRepository // <1>
interface FruitRepository extends CrudRepository<Fruit, String> {

    @NonNull
    Iterable<Fruit> findByNameInList(@NonNull List<String> names) // <2>
}
