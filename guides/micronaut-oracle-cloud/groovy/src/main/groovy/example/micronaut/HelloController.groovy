package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller
class HelloController {

    @Get(value = '/', produces = MediaType.TEXT_PLAIN)
    String index() {
        'Micronaut on Oracle Cloud'
    }
}
