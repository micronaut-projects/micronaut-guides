package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;

@Secured(SecurityRule.IS_AUTHENTICATED) // <1>
@Controller  // <2>
public class HomeController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get  // <3>
    String index(Principal principal) {  // <4>
        return principal.getName();
    }
}
