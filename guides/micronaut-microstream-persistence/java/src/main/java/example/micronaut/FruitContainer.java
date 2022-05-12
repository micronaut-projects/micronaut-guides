package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Introspected // <1>
public class FruitContainer {

    private final Map<String, Fruit> fruits = new ConcurrentHashMap<>();

    public Map<String, Fruit> getFruits() {
        return fruits;
    }
}
