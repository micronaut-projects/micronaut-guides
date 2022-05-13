package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotEmpty

@Introspected // <1>
data class Fruit(
    val name: @NotEmpty String, // <2>
    var description: String? // <3>
)
