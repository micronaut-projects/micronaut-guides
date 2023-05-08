package example.micronaut.genre

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
class GenreUpdateCommand(var id: Long, @NotBlank var name: String)
