package example.micronaut

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected // <1>
data class GenreUpdateCommand(
    val id: Long,
    @field:NotBlank val name: String
)
