package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class Book(val isbn: @NotBlank String,
                val name: @NotBlank String)
