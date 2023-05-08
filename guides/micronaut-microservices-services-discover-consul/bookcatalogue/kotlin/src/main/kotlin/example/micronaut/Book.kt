package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
data class Book(@NotBlank val isbn: String,
                @NotBlank val name: String)
