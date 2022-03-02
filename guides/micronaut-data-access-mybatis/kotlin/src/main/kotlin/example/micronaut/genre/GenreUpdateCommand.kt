package example.micronaut.genre

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class GenreUpdateCommand(var id: Long, @NotBlank var name: String)
