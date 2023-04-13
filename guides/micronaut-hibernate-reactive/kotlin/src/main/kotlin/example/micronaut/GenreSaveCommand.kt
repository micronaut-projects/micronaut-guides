package example.micronaut

import io.micronaut.serde.annotation.Serdeable

import javax.validation.constraints.NotBlank

@Serdeable // <1>
class GenreSaveCommand(@field:NotBlank var name: String)