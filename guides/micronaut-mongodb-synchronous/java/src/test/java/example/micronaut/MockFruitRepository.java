package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@Requires(property = "spec.name", value = "FruitControllerValidationTest")
@Singleton
@Replaces(FruitRepository.class)
class MockFruitRepository implements FruitRepository {

    @Override
    public List<Fruit> list() {
        return Collections.emptyList();
    }

    @Override
    public void save(Fruit fruit) {
    }
}
