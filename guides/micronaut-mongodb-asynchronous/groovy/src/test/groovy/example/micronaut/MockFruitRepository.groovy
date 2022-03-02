package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.async.publisher.Publishers
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Requires(property = 'spec.name', value = 'FruitControllerValidationSpec')
@Singleton
@Replaces(FruitRepository)
class MockFruitRepository implements FruitRepository {

    @Override
    Publisher<Fruit> list() {
        Publishers.empty()
    }

    @Override
    Mono<Boolean> save(Fruit fruit) {
        Mono.just(false)
    }
}
