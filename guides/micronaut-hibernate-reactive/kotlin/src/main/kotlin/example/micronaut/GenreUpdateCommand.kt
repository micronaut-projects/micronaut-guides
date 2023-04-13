package example.micronaut

import io.micronaut.serde.annotation.Serdeable

import javax.validation.constraints.NotBlank

@Serdeable
class GenreUpdateCommand(var id: Long, @field:NotBlank var name: String?)