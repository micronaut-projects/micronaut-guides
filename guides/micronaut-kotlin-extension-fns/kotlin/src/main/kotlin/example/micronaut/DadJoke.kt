package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable // <1>
data class DadJoke(val id: String, val joke: String, val status: Int)
