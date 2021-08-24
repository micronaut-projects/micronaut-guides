package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

@Introspected
data class GithubRepo @Creator constructor(val name: String)
