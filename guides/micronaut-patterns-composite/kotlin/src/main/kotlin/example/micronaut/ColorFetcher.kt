package example.micronaut

import io.micronaut.core.order.Ordered
import io.micronaut.http.HttpRequest
import java.util.Optional

@FunctionalInterface // <1>
interface ColorFetcher : Ordered { // <2>

    fun favouriteColor(request: HttpRequest<*>): Optional<String>
}
