package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller
class HomeController {
    @Get
    @View("index.html")
    Map<String, Object> index() {
        return Collections.emptyMap();
    }
}
