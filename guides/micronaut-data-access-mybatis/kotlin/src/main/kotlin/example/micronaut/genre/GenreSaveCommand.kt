package example.micronaut.genre

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class GenreSaveCommand(@NotBlank var name: String)
