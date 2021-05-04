package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.reactivex.Maybe
import java.net.URI
import javax.inject.Singleton

@Singleton // <1>
class GithubLowLevelClient(@param:Client(GithubConfiguration.GITHUB_API_URL) private val httpClient: RxHttpClient,  // <2>
                           configuration: GithubConfiguration) {  // <3>
    private val uri: URI = UriBuilder.of("/repos")
        .path(configuration.organization)
        .path(configuration.repository)
        .path("releases")
        .build()

    fun fetchReleases(): Maybe<List<GithubRelease>> {
        val req: HttpRequest<*> = HttpRequest.GET<Any>(uri) // <4>
            .header("User-Agent", "Micronaut HTTP Client") // <5>
        val flowable = httpClient.retrieve(req, Argument.listOf(GithubRelease::class.java)) // <6>
        return flowable.firstElement() // <7>
    }
}
