package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.Collections;
import java.util.Map;

@Controller // <1>
public class HelloController {

    @Get // <2>
    public Map<String, Object> index() {
        return Collections.singletonMap("message", "Hello World"); // <3>
    }
}
