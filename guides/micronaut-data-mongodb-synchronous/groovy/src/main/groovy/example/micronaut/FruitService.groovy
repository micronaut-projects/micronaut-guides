package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.inject.Singleton

import java.util.List
import java.util.Optional

@Singleton // <1>
@ExecuteOn(TaskExecutors.IO)  // <2>
class FruitService {

    private final FruitRepository fruitRepository;

    FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository
    }

    Iterable<Fruit> list() {
        fruitRepository.findAll()
    }

    Fruit save(Fruit fruit) {
        if (fruit.getId() == null) {
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
