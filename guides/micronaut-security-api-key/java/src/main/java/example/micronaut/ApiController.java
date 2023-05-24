package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;

@Controller("/api") // <1>
class ApiController {

    @Produces(MediaType.TEXT_PLAIN) // <2>
    @Get // <3>
    @Secured(SecurityRule.IS_AUTHENTICATED) // <4>
    String index(Principal principal) { // <5>
        return "Hello " + principal.getName();
    }
}
