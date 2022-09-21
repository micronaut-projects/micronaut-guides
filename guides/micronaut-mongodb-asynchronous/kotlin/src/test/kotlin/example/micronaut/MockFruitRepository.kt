package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.async.publisher.Publishers
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Requires(property = "spec.name", value = "FruitControllerValidationTest")
@Singleton
@Replaces(FruitRepository::class)
class MockFruitRepository : FruitRepository {

    override fun list(): Publisher<Fruit> = Publishers.empty()

    override fun save(fruit: Fruit): Mono<Boolean> = Mono.just(false)
}
