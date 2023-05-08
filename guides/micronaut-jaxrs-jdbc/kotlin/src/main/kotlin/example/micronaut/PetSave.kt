package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
data class PetSave(@NotBlank val name: String, val type: PetType)