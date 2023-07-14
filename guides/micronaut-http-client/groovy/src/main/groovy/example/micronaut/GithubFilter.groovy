package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.ClientFilter
import io.micronaut.http.annotation.RequestFilter

@ClientFilter("/repos/**") // <1>
@Requires(property = GithubConfiguration.PREFIX + ".username") // <2>
@Requires(property = GithubConfiguration.PREFIX + ".token") // <2>
class GithubFilter {

    private final GithubConfiguration configuration

    GithubFilter(GithubConfiguration configuration) { // <3>
        this.configuration = configuration
    }

    @RequestFilter // <4>
    void doFilter(MutableHttpRequest<?> request) {
        request.basicAuth(configuration.username, configuration.token) // <5>
    }
}
