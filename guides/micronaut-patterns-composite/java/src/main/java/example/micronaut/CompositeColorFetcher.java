package example.micronaut;

import io.micronaut.context.annotation.Primary;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Primary // <1>
@Singleton // <2>
public class CompositeColorFetcher implements ColorFetcher {

    private final List<ColorFetcher> colorFetcherList;

    public CompositeColorFetcher(List<ColorFetcher> colorFetcherList) { // <3>
        this.colorFetcherList = colorFetcherList;
    }

    @Override
    public Optional<String> favouriteColor(HttpRequest<?> request) {
        return colorFetcherList.stream()
                .map(colorFetcher -> colorFetcher.favouriteColor(request))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
