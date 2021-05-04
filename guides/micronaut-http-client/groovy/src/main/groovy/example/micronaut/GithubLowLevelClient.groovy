package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.http.uri.UriTemplate
import io.reactivex.Flowable
import io.reactivex.Maybe

import javax.inject.Singleton

@Singleton // <1>
@CompileStatic
class GithubLowLevelClient {

    private final RxHttpClient httpClient
    private final URI uri

    GithubLowLevelClient(@Client(GithubConfiguration.GITHUB_API_URL) RxHttpClient httpClient,  // <2>
                                GithubConfiguration configuration) {  // <3>
        this.httpClient = httpClient
        this.uri = UriBuilder.of("/repos")
            .path(configuration.getOrganization())
            .path(configuration.getRepository())
            .path("releases")
            .build()
    }

    Maybe<List<GithubRelease>> fetchReleases() {
        HttpRequest<?> req = HttpRequest.GET(uri) // <4>
            .header("User-Agent", "Micronaut HTTP Client") // <5>
        Flowable<List<GithubRelease>> flowable = httpClient.retrieve(req, Argument.listOf(GithubRelease)) // <6>
        return flowable.firstElement() // <7>
    }
}
