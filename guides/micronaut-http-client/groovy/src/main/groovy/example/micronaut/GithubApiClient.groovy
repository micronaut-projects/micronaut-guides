package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import org.reactivestreams.Publisher;

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.HttpHeaders.USER_AGENT

@Client(id = "github") // <1>
@Header(name = USER_AGENT, value = "Micronaut HTTP Client") // <2>
@Header(name = ACCEPT, value = "application/vnd.github.v3+json, application/json") // <3>
interface GithubApiClient {

    @Get('/repos/${github.organization}/${github.repo}/releases') // <4>
    Publisher<GithubRelease> fetchReleases() // <5>
}
