package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Controller("/github") // <1>
class GithubController(private val githubLowLevelClient: GithubLowLevelClient, // <2>
                       private val githubApiClient: GithubApiClient) {

    @Get("/releases-lowlevel") // <3>
    fun releasesWithLowLevelClient(): Mono<List<GithubRelease>> { // <4>
        return githubLowLevelClient.fetchReleases()
    }

    @Get(uri = "/releases", produces = [MediaType.APPLICATION_JSON_STREAM]) // <5>
    fun fetchReleases(): Publisher<GithubRelease?>? { // <6>
        return githubApiClient.fetchReleases()
    }
}
