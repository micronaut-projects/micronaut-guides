package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.ClientFilter
import io.micronaut.http.annotation.RequestFilter

@ClientFilter("/repos/**") // <1>
@Requires(condition = GithubFilterCondition::class) // <2>
class GithubFilter(val configuration: GithubConfiguration) { // <3>

    @RequestFilter // <4>
    fun doFilter(request: MutableHttpRequest<*>) {
        request.basicAuth(configuration.username, configuration.token) // <5>
    }
}
