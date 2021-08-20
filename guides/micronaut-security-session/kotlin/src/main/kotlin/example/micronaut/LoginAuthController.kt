package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Secured(SecurityRule.IS_ANONYMOUS) // <1>
@Controller("/login") // <2>
class LoginAuthController {

    @Produces(MediaType.TEXT_HTML)
    @Get("/auth") // <3>
    @View("auth") // <4>
    fun auth(): Map<String, Any> = emptyMap()

    @Produces(MediaType.TEXT_HTML)
    @Get("/authFailed") // <5>
    @View("auth") // <4>
    fun authFailed(): Map<String, Any> = mapOf("errors" to true)
}
