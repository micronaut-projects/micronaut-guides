package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import io.micronaut.core.async.annotation.SingleResult

@CompileStatic
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
    @SingleResult
    Publisher<List<GithubRelease>> releasesWithLowLevelClient() { // <4>
        githubLowLevelClient.fetchReleases()
    }

    @Get(uri = "/releases", produces = MediaType.APPLICATION_JSON_STREAM) // <5>
    Publisher<GithubRelease> fetchReleases() { // <6>
        githubApiClient.fetchReleases()
    }
}
