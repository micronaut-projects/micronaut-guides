package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.publisher.Publishers;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Requires(property = "spec.name", value = "FruitControllerValidationTest")
@Singleton
@Replaces(FruitRepository.class)
class MockFruitRepository implements FruitRepository {

    @Override
    public Publisher<Fruit> list() {
        return Publishers.empty();
    }

    @Override
    public Mono<Boolean> save(Fruit fruit) {
        return Mono.just(false);
    }
}
