package example.micronaut.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.net.URI;
import java.net.URISyntaxException;

@Controller // <1>
public class HomeController {
    @Get
    HttpResponse<?> index() throws URISyntaxException {
        return HttpResponse.seeOther(new URI("/rooms"));
    }
}
