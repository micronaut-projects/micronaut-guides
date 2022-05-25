package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton

@Singleton // <1>
@CompileStatic
class DefaultFruitService implements FruitService {

    private final FruitRepository fruitRepository

    DefaultFruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository
    }

    Iterable<Fruit> list() {
        fruitRepository.findAll()
    }

    Fruit save(Fruit fruit) {
        if (fruit.id == null) {
            fruitRepository.save(fruit)
        } else {
            fruitRepository.update(fruit)
        }
    }

    Optional<Fruit> find(@NonNull String id) {
        fruitRepository.findById(id)
    }

    Iterable<Fruit> findByNameInList(List<String> name) {
        fruitRepository.findByNameInList(name)
    }
}
