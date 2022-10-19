package example.micronaut

import io.micronaut.core.annotation.NonNull

import java.util.concurrent.ConcurrentHashMap

class FruitContainer {

    @NonNull
    final Map<String, Fruit> fruits = new ConcurrentHashMap<>()
}
