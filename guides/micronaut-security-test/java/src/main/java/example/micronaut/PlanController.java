package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

@Controller // <1>
class PlanController {

    @Produces(MediaType.TEXT_PLAIN) // <2>
    @Secured(SecurityRule.IS_AUTHENTICATED) // <3>
    @Get("/plan") // <4>
    String index(Authentication authentication) { // <5>
        if (authentication.getRoles().contains("ROLE_EVIL_MASTERMIND")) {
            return "Kill Sherlock Holmes and his companions";
        }
        return "Plan New Year";
    }
}
