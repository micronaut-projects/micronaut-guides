package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.Creator

@Introspected
data class GithubUser @Creator constructor(val login: String, val name: String, val email: String)
