package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.List;

@Singleton // <1>
class DefaultFruitService implements FruitService {

    private final FruitRepository fruitRepository;

    public DefaultFruitService(FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
    }

    public Publisher<Fruit> list() {
        return fruitRepository.findAll();
    }

    public Publisher<Fruit> save(Fruit fruit) {
        if (fruit.getId() == null) {
            return fruitRepository.save(fruit);
        } else {
            return fruitRepository.update(fruit);
        }
    }

    public Publisher<Fruit> find(@NonNull String id) {
        return fruitRepository.findById(id);
    }

    public Publisher<Fruit> findByNameInList(List<String> name) {
        return fruitRepository.findByNameInList(name);
    }
}
