package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class GithubUser(val login: String, val name: String, val email: String)
