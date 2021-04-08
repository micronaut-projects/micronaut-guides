package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

@Controller("/")
public class HelloController {

    @Get(value = "/", produces = MediaType.TEXT_PLAIN)
    public String index() {
        return "Micronaut on Azure";
    }
}