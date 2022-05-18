package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull

import java.util.concurrent.ConcurrentHashMap

@Introspected // <1>
class FruitContainer {

    @NonNull
    final Map<String, Fruit> fruits = new ConcurrentHashMap<>()
}
