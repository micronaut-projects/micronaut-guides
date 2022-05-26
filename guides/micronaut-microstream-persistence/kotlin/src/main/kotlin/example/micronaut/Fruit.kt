package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected // <1>
data class Fruit(
    @field:NotBlank val name: String, // <2>
    var description: String? // <3>
)
