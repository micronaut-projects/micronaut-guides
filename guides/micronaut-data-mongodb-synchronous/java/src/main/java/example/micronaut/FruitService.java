package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton // <1>
@ExecuteOn(TaskExecutors.IO)  // <2>
public class FruitService {

    private final FruitRepository fruitRepository;

    public FruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    Iterable<Fruit> list() {
        return fruitRepository.findAll();
    }

    Fruit save(Fruit fruit) {
        if (fruit.getId() == null) {
            return fruitRepository.save(fruit);
        } else {
            return fruitRepository.update(fruit);
        }
    }

    public Optional<Fruit> find(@NonNull String id) {
        return fruitRepository.findById(id);
    }

    public Iterable<Fruit> findByNameInList(List<String> name) {
        return fruitRepository.findByNameInList(name);
    }
}
