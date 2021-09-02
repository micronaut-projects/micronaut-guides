package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected
data class GithubRepo(val name: String)
