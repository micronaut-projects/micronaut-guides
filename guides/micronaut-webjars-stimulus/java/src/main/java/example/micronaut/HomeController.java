package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller // <1>
class HomeController {
    @Get // <2>
    @View("index.html") // <3>
    Map<String, Object> index() {
        return Collections.emptyMap();
    }
}
