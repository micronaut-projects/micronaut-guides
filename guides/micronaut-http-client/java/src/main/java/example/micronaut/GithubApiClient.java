package example.micronaut;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Flowable;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Client(GithubConfiguration.GITHUB_API_URL) // <1>
@Header(name = USER_AGENT, value = "Micronaut HTTP Client") // <2>
@Header(name = ACCEPT, value = "application/vnd.github.v3+json, application/json") // <3>
public interface GithubApiClient {

    @Get("/repos/${github.organization}/${github.repo}/releases") // <4>
    Flowable<GithubRelease> fetchReleases(); // <5>
}
