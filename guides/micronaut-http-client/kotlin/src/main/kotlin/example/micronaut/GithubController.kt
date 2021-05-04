package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import io.reactivex.Maybe

@Controller("/github") // <1>
class GithubController(private val githubLowLevelClient: GithubLowLevelClient, // <2>
                       private val githubApiClient: GithubApiClient) {

    @Get("/releases-lowlevel") // <3>
    fun releasesWithLowLevelClient(): Maybe<List<GithubRelease>> { // <4>
        return githubLowLevelClient.fetchReleases()
    }

    @Get(uri = "/releases", produces = [MediaType.APPLICATION_JSON_STREAM]) // <5>
    fun packages(): Flowable<GithubRelease?>? { // <6>
        return githubApiClient.fetchReleases()
    }
}
