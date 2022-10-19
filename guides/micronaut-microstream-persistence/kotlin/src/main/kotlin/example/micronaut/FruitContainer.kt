package example.micronaut

import java.util.concurrent.ConcurrentHashMap

class FruitContainer {

    val fruits: MutableMap<String, Fruit> = ConcurrentHashMap()
}