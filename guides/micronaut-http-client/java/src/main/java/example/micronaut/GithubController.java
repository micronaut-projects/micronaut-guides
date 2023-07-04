package example.micronaut;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.reactivestreams.Publisher;
import java.util.List;

@Controller("/github") // <1>
public class GithubController {

    private final GithubLowLevelClient githubLowLevelClient;
    private final GithubApiClient githubApiClient;
    public GithubController(GithubLowLevelClient githubLowLevelClient,
                            GithubApiClient githubApiClient) { // <2>
        this.githubLowLevelClient = githubLowLevelClient;
        this.githubApiClient = githubApiClient;
    }

    @Get("/releases-lowlevel") // <3>
    @SingleResult // <4>
    Publisher<List<GithubRelease>> releasesWithLowLevelClient() {
        return githubLowLevelClient.fetchReleases();
    }

    @Get("/releases") // <5>
    @SingleResult // <4>
    Publisher<List<GithubRelease>> fetchReleases() { // <6>
        return githubApiClient.fetchReleases();
    }
}
