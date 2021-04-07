package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected // <1>
data class DadJoke(val id: String, val joke: String, val status: Int)
