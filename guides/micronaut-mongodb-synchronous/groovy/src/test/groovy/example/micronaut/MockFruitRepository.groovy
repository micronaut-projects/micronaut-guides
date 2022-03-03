package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton

@Requires(property = 'spec.name', value = 'FruitControllerValidationSpec')
@Singleton
@Replaces(FruitRepository)
class MockFruitRepository implements FruitRepository {

    @Override
    List<Fruit> list() {
        []
    }

    @Override
    void save(Fruit fruit) {
    }
}
