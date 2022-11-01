package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class GithubRelease(val name: String, val url: String)
