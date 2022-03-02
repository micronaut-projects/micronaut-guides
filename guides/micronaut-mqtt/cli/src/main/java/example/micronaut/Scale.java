package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum Scale {
    FAHRENHEIT("Fahrenheit"),
    CELSIUS("Celsius");

    private static final Map<String,Scale> ENUM_MAP;
    private final String name;

    Scale(String name) {
        this.name = name;
    }

    static {
        Map<String,Scale> map = new ConcurrentHashMap<>();
        for (Scale instance : Scale.values()) {
            map.put(instance.getName(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    @NonNull
    public static Optional<Scale> of(@NonNull String name) {
        return Optional.ofNullable(ENUM_MAP.get(name));
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
