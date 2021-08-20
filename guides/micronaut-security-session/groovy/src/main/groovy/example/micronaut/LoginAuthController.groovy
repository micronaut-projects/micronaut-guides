package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@CompileStatic
@Secured(SecurityRule.IS_ANONYMOUS) // <1>
@Controller("/login") // <2>
class LoginAuthController {

    @Produces(MediaType.TEXT_HTML)
    @Get("/auth") // <3>
    @View("auth") // <4>
    Map<String, Object> auth() {
        Collections.emptyMap()
    }

    @Produces(MediaType.TEXT_HTML)
    @Get("/authFailed") // <5>
    @View("auth") // <4>
    Map<String, Object> authFailed() {
        [errors: true] as Map
    }
}
