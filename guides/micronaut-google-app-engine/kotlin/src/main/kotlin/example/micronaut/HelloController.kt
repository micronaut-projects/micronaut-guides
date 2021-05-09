package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class HelloController {
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    fun index(): String {
        return "Micronaut on App Engine"
    }
}