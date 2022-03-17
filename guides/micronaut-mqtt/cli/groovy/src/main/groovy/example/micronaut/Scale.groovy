package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull

import java.util.concurrent.ConcurrentHashMap

@CompileStatic
enum Scale {
    FAHRENHEIT('Fahrenheit'),
    CELSIUS('Celsius')

    private static final Map<String,Scale> ENUM_MAP

    final String name

    Scale(String name) {
        this.name = name
    }

    static {
        Map<String,Scale> map = new ConcurrentHashMap<>()
        for (Scale instance : Scale.values()) {
            map[instance.name] = instance
        }
        ENUM_MAP = Collections.unmodifiableMap(map)
    }

    @NonNull
    static Optional<Scale> of(@NonNull String name) {
        return Optional.ofNullable(ENUM_MAP.get(name))
    }

    @Override
    String toString() {
        name
    }

    static Set<String> candidates() {
        ENUM_MAP.keySet()
    }
}
