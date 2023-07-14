package example.micronaut

import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher

@Controller("/github") // <1>
class GithubController {

    private final GithubLowLevelClient githubLowLevelClient
    private final GithubApiClient githubApiClient

    GithubController(GithubLowLevelClient githubLowLevelClient,
                            GithubApiClient githubApiClient) { // <2>
        this.githubLowLevelClient = githubLowLevelClient
        this.githubApiClient = githubApiClient
    }

    @Get("/releases-lowlevel") // <3>
    @SingleResult // <4>
    Publisher<List<GithubRelease>> releasesWithLowLevelClient() {
        githubLowLevelClient.fetchReleases()
    }

    @Get("/releases") // <5>
    @SingleResult // <4>
    Publisher<List<GithubRelease>> fetchReleases() {
        githubApiClient.fetchReleases()
    }
}
