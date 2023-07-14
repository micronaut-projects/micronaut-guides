package example.micronaut

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher

@Controller("/github") // <1>
class GithubController(private val githubLowLevelClient: GithubLowLevelClient, // <2>
                       private val githubApiClient: GithubApiClient) {

    @Get("/releases-lowlevel") // <3>
    @SingleResult // <4>
    fun releasesWithLowLevelClient(): Publisher<List<GithubRelease>> {
        return githubLowLevelClient.fetchReleases()
    }

    @Get("/releases") // <5>
    @SingleResult // <4>
    fun fetchReleases(): Publisher<List<GithubRelease>> {
        return githubApiClient.fetchReleases()
    }
}
