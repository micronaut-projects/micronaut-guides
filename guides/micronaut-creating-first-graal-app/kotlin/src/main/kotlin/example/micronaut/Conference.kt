package example.micronaut

import io.micronaut.serde.annotation.Serdeable;

@Serdeable // <1>
data class Conference(val name: String)
