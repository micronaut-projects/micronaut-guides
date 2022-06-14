package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Primary
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton

@CompileStatic
@Primary // <1>
@Singleton // <2>
class CompositeColorFetcher implements ColorFetcher {

    private final List<ColorFetcher> colorFetcherList

    CompositeColorFetcher(List<ColorFetcher> colorFetcherList) { // <3>
        this.colorFetcherList = colorFetcherList
    }

    @Override
    Optional<String> favouriteColor(HttpRequest<?> request) {
        return Optional.ofNullable(colorFetcherList.collect { it.favouriteColor(request) }
                .findAll { it.isPresent() }
                .collect { it.get() }[0])
    }
}
