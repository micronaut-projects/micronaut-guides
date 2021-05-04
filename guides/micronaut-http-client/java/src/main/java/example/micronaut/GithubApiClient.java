package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

@Client(GithubConfiguration.GITHUB_API_URL) // <1>
@Header(name = "User-Agent", value = "Micronaut HTTP Client") // <2>
public interface GithubApiClient {

    @Get("/repos/${github.organization}/${github.repository}/releases") // <3>
    Flowable<GithubRelease> fetchReleases(); // <4>
}
