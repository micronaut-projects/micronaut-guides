package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
data class BookRecommendation(@NotBlank val name: String)
