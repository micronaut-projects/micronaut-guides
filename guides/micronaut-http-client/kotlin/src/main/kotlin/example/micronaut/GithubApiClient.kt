package example.micronaut

import io.micronaut.http.HttpHeaders.ACCEPT
import io.micronaut.http.HttpHeaders.USER_AGENT
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Headers
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher;

@Client(id = "github") // <1>
@Headers(
    Header(name = USER_AGENT, value = "Micronaut HTTP Client"), // <2>
    Header(name = ACCEPT, value = "application/vnd.github.v3+json, application/json") // <3>
)
interface GithubApiClient {

    @Get("/repos/\${github.organization}/\${github.repo}/releases") // <4>
    fun fetchReleases(): Publisher<GithubRelease?>? // <5>
}
