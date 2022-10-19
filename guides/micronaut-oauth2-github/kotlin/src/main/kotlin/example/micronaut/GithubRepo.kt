package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class GithubRepo(val name: String)
