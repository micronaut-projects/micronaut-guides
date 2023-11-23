package example.micronaut

import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
data class FruitCommand(
    @field:NotBlank val name: String, // <2>
    val description: String? = null // <3>
)