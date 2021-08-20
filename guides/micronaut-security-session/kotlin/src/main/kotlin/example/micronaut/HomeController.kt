package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.security.Principal

@Secured(SecurityRule.IS_ANONYMOUS) // <1>
@Controller // <2>
class HomeController {

    @Produces(MediaType.TEXT_HTML)
    @Get // <3>
    @View("home") // <4>
    fun index(principal: Principal?): Map<String, Any> { // <5>
        val data = mutableMapOf<String, Any>("loggedIn" to (principal != null))
        if (principal != null) {
            data["username"] = principal.name
        }
        return data
    }
}
