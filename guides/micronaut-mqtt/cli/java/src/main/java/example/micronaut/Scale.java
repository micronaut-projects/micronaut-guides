/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        return name;
    }

    public static Set<String> candidates() {
        return ENUM_MAP.keySet();
    }
}
