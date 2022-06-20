package example.micronaut

import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import org.reactivestreams.Publisher

@Singleton // <1>
class DefaultFruitService implements FruitService {

    private final FruitRepository fruitRepository

    DefaultFruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository
    }

    Publisher<Fruit> list() {
        fruitRepository.findAll()
    }

    Publisher<Fruit> save(Fruit fruit) {
        if (fruit.getId() == null) {
            fruitRepository.save(fruit)
        } else {
            fruitRepository.update(fruit)
        }
    }

    Publisher<Fruit> find(@NonNull String id) {
        fruitRepository.findById(id)
    }

    Publisher<Fruit> findByNameInList(List<String> name) {
        fruitRepository.findByNameInList(name)
    }
}
