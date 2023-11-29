package example.micronaut.controllers;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;

import java.util.Collections;
import java.util.Map;

@Controller // <1>
public class HomeController {

    @Produces(MediaType.TEXT_HTML) // <2>
    @Secured(SecurityRule.IS_ANONYMOUS) // <3>
    @Get // <4>
    @View("home.html") // <5>
    Map<String, Object> index() {
        return Collections.emptyMap();
    }
}
