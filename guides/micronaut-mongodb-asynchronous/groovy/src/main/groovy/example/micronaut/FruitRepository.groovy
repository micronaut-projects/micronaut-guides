package example.micronaut

import io.micronaut.core.annotation.NonNull
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

import javax.validation.Valid
import javax.validation.constraints.NotNull

interface FruitRepository {

    @NonNull
    Publisher<Fruit> list()

    Mono<Boolean> save(@NonNull @NotNull @Valid Fruit fruit) // <1>
}
