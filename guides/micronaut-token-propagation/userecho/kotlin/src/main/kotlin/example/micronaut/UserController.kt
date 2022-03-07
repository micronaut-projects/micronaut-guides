package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import java.security.Principal

@Controller("/user") // <1>
class UserController {

    @Secured(IS_AUTHENTICATED) // <2>
    @Produces(TEXT_PLAIN) // <3>
    @Get // <4>
    fun index(principal: Principal): String = principal.name // <5>
}
