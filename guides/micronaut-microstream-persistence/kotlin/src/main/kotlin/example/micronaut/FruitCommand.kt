package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected // <1>
data class FruitCommand(
    @field:NotBlank val name: String, // <2>
    val description: String? = null // <3>
)