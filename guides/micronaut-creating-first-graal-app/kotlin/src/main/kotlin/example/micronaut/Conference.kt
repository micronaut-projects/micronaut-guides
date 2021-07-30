package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected // <1>
data class Conference(val name: String)
