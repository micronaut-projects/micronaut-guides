package example.micronaut

import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
data class Fruit(
    @field:NotBlank val name: String, // <2>
    var description: String? // <3>
)
