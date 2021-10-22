package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class Book(@NotBlank val isbn: String,
                @NotBlank val name: String)
