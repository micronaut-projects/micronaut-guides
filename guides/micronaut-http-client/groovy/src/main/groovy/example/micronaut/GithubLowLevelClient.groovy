package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.reactivex.Flowable
import io.reactivex.Maybe

import javax.inject.Singleton

import static io.micronaut.http.HttpHeaders.ACCEPT
import static io.micronaut.http.HttpHeaders.USER_AGENT

@Singleton // <1>
@CompileStatic
class GithubLowLevelClient {

    private final HttpClient httpClient
    private final URI uri

    GithubLowLevelClient(@Client(GithubConfiguration.GITHUB_API_URL) HttpClient httpClient,  // <2>
                         GithubConfiguration configuration) {  // <3>
        this.httpClient = httpClient
        this.uri = UriBuilder.of("/repos")
            .path(configuration.getOrganization())
            .path(configuration.getRepo())
            .path("releases")
            .build()
    }

    Maybe<List<GithubRelease>> fetchReleases() {
        HttpRequest<?> req = HttpRequest.GET(uri) // <4>
            .header(USER_AGENT, "Micronaut HTTP Client") // <5>
            .header(ACCEPT, "application/vnd.github.v3+json, application/json") // <6>
        Flowable<List<GithubRelease>> flowable = httpClient.retrieve(req, Argument.listOf(GithubRelease)) // <7>
        return flowable.firstElement() // <8>
    }
}
