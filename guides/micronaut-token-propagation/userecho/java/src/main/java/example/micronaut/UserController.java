package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;

@Controller("/user") // <1>
public class UserController {

    @Secured(SecurityRule.IS_AUTHENTICATED) // <2>
    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get // <4>
    String index(Principal principal) { // <5>
        return principal.getName();
    }
}
