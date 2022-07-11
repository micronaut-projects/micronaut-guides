package example.micronaut

import io.micronaut.core.annotation.Introspected
import java.util.concurrent.ConcurrentHashMap

@Introspected // <1>
class FruitContainer {

    val fruits: MutableMap<String, Fruit> = ConcurrentHashMap()
}