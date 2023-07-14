package example.micronaut

import jakarta.validation.constraints.NotBlank
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Book(@NotBlank val isbn: String,
                @NotBlank val name: String)
