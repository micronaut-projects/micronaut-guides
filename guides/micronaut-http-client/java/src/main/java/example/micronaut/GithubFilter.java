package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.ClientFilter;
import io.micronaut.http.annotation.RequestFilter;

@ClientFilter("/repos/**") // <1>
@Requires(property = GithubConfiguration.PREFIX + ".username") // <2>
@Requires(property = GithubConfiguration.PREFIX + ".token") // <2>
public class GithubFilter {

    private final GithubConfiguration configuration;

    public GithubFilter(GithubConfiguration configuration) { // <3>
        this.configuration = configuration;
    }

    @RequestFilter // <4>
    public void doFilter(MutableHttpRequest<?> request) {
        request.basicAuth(configuration.username(), configuration.token()); // <5>
    }
}
