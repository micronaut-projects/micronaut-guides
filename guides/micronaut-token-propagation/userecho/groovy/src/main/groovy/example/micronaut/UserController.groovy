package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured

import java.security.Principal

import static io.micronaut.http.MediaType.TEXT_PLAIN
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED

@CompileStatic
@Controller('/user') // <1>
class UserController {

    @Secured(IS_AUTHENTICATED) // <2>
    @Produces(TEXT_PLAIN) // <3>
    @Get // <4>
    String index(Principal principal) { // <5>
        return principal.name
    }
}
