package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller // <1>
class HelloController {

    @Get // <2>
    Map<String, Object> index() {
        [message: "Hello World"] // <3>
    }
}
