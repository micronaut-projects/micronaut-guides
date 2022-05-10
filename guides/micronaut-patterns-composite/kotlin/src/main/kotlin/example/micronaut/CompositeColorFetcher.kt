package example.micronaut

import io.micronaut.context.annotation.Primary
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton
import java.util.Optional

@Primary // <1>
@Singleton // <2>
class CompositeColorFetcher(private val colorFetcherList: List<ColorFetcher>) : ColorFetcher { // <3>

    override fun favouriteColor(request: HttpRequest<*>): Optional<String> =
        colorFetcherList.stream()
            .map { it.favouriteColor(request) }
            .filter { it.isPresent }
            .map { it.get() }
            .findFirst()
}
