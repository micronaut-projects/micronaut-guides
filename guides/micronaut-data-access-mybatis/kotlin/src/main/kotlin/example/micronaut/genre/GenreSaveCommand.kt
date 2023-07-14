package example.micronaut.genre

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
data class GenreSaveCommand(@NotBlank var name: String)
