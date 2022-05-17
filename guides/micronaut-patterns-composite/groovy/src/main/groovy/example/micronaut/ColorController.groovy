package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@CompileStatic
@Controller('/color') // <1>
class ColorController {

    private final ColorFetcher colorFetcher

    ColorController(ColorFetcher colorFetcher) { // <2>
        this.colorFetcher = colorFetcher
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get('/mint') // <4>
    Optional<String> mint(@NonNull HttpRequest<?> request) { // <5>
        return colorFetcher.favouriteColor(request)
    }

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get
    Optional<String> index(@NonNull HttpRequest<?> request) { // <5>
        return colorFetcher.favouriteColor(request)
    }
}
