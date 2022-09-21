package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
class GenreUpdateCommand(var id: Long, @field:NotBlank var name: String?)