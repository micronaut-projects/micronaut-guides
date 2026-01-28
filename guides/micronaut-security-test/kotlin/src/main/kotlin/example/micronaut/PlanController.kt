package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule

@Controller // <1>
class PlanController {

    @Produces(MediaType.TEXT_PLAIN) // <2>
    @Secured(SecurityRule.IS_AUTHENTICATED) // <3>
    @Get("/plan") // <4>
    fun index(authentication: Authentication): String = // <5>
        if ("ROLE_EVIL_MASTERMIND" in authentication.roles) {
            "Kill Sherlock Holmes and his companions"
        } else {
            "Plan New Year"
        }
}
