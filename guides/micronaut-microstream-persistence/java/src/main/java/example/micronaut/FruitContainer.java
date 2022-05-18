package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Introspected // <1>
public class FruitContainer {
    @NonNull
    private final Map<String, Fruit> fruits = new ConcurrentHashMap<>();

    @NonNull
    public Map<String, Fruit> getFruits() {
        return fruits;
    }
}
