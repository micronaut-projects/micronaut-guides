package example.micronaut

import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton
import java.util.Optional

@Singleton // <1>
class HttpHeaderColorFetcher : ColorFetcher {

    override fun favouriteColor(request: HttpRequest<*>): Optional<String> =
        request.headers.get("color", String::class.java)

    override fun getOrder(): Int = 10 // <2>
}
