package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

import javax.inject.Singleton;
import java.net.URI;
import java.util.List;

import static io.micronaut.http.HttpHeaders.ACCEPT;
import static io.micronaut.http.HttpHeaders.USER_AGENT;

@Singleton // <1>
public class GithubLowLevelClient {

    private final RxHttpClient httpClient;
    private final URI uri;

    public GithubLowLevelClient(@Client(GithubConfiguration.GITHUB_API_URL) RxHttpClient httpClient,  // <2>
                                GithubConfiguration configuration) {  // <3>
        this.httpClient = httpClient;
        this.uri = UriBuilder.of("/repos")
                .path(configuration.getOrganization())
                .path(configuration.getRepo())
                .path("releases")
                .build();
    }

    Maybe<List<GithubRelease>> fetchReleases() {
        HttpRequest<?> req = HttpRequest.GET(uri) // <4>
                .header(USER_AGENT, "Micronaut HTTP Client") // <5>
                .header(ACCEPT, "application/vnd.github.v3+json, application/json"); // <6>
        Flowable<List<GithubRelease>> flowable = httpClient.retrieve(req, Argument.listOf(GithubRelease.class)); // <7>
        return flowable.firstElement(); // <8>
    }
}
