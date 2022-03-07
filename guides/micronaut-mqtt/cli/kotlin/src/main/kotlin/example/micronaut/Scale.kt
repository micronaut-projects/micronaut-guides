package example.micronaut

import java.util.Collections
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

enum class Scale(val scaleName: String) {
    FAHRENHEIT("Fahrenheit"),
    CELSIUS("Celsius");

    override fun toString(): String = scaleName

    companion object {
        private var ENUM_MAP: Map<String, Scale>

        init {
            val map: MutableMap<String, Scale> = ConcurrentHashMap()
            for (instance in values()) {
                map[instance.scaleName] = instance
            }
            ENUM_MAP = Collections.unmodifiableMap(map)
        }

        fun of(name: String): Optional<Scale> = Optional.ofNullable(ENUM_MAP[name])

        fun candidates(): Set<String> = ENUM_MAP.keys
    }
}
