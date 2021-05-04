package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import io.reactivex.Maybe

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
    Maybe<List<GithubRelease>> releasesWithLowLevelClient() { // <4>
        return githubLowLevelClient.fetchReleases()
    }

    @Get(uri = "/releases", produces = MediaType.APPLICATION_JSON_STREAM) // <5>
    Flowable<GithubRelease> packages() { // <6>
        return githubApiClient.fetchReleases()
    }
}
