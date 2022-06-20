package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class BookRecommendation(@NotBlank val name: String)
