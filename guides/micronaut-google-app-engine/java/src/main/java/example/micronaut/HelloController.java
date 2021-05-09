package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller
public class HelloController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get
    public String index() {
        return "Micronaut on App Engine";
    }
}