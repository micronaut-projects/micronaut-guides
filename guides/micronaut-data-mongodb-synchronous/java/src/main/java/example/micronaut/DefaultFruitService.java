package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton // <1>
class DefaultFruitService implements FruitService {

    private final FruitRepository fruitRepository;

    public DefaultFruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    public Iterable<Fruit> list() {
        return fruitRepository.findAll();
    }

    public Fruit save(Fruit fruit) {
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
