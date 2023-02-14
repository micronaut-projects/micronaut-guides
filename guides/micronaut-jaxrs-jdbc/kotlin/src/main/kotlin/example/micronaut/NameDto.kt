package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
data class NameDto(val name: String)