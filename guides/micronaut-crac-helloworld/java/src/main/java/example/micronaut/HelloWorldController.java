package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Collections;
import java.util.Map;

@Controller // <1>
class HelloWorldController {

    @Get // <2>
    Map<String, String> index() {
        return Collections.singletonMap("message", "Hello World");
    }
}
