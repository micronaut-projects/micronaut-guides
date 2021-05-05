package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class HelloController {

    @Get(produces = [MediaType.TEXT_PLAIN])
    fun index() = "Micronaut on Oracle Cloud"
}
