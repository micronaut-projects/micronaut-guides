package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import java.util.Optional

@Controller("/color") // <1>
class ColorController(private val colorFetcher: ColorFetcher) { // <2>

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/mint") // <4>
    fun mint(request: HttpRequest<*>): Optional<String> = // <5>
        colorFetcher.favouriteColor(request)

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get
    fun index(request: HttpRequest<*>): Optional<String> = // <5>
        colorFetcher.favouriteColor(request)
}
