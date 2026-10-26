package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@CompileStatic
@Controller("/account") // <1>
class AccountController {

    @Secured(SecurityRule.IS_AUTHENTICATED) // <2>
    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get // <4>
    String index() {
        "Your account"
    }
}
