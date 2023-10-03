package example.micronaut;

import io.micronaut.core.annotation.ReflectionConfig;

@ReflectionConfig(
        type = StringReverser.class,
        methods = {
                @ReflectionConfig.ReflectiveMethodConfig(name = "reverse", parameterTypes = {String.class})
        }
)
@ReflectionConfig(
        type = StringCapitalizer.class,
        methods = {
                @ReflectionConfig.ReflectiveMethodConfig(name = "capitalize", parameterTypes = {String.class})
        }
)
class GraalConfig {
}
