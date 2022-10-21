package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import javax.validation.constraints.NotBlank

@Serdeable // <1>
data class GenreUpdateCommand(
    val id: Long,
    @field:NotBlank val name: String
)
