package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

@Controller
class HelloController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    String index() {
        'Micronaut on App Engine'
    }
}