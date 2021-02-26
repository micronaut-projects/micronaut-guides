package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import org.reactivestreams.Publisher;

@Filter("/api/${bintray.apiversion}/repos/**") // <1>
@Requires(property = BintrayConfiguration.PREFIX + ".username") // <2>
@Requires(property = BintrayConfiguration.PREFIX + ".token") // <2>
public class BintrayFilter implements HttpClientFilter {

    private final BintrayConfiguration configuration;

    public BintrayFilter(BintrayConfiguration configuration) { // <3>
        this.configuration = configuration;
    }

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        return chain.proceed(request.basicAuth(configuration.getUsername(), configuration.getToken())); // <4>
    }
}
