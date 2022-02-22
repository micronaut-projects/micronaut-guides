package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Controller // <1>
class HomeController {

    @Secured(SecurityRule.IS_ANONYMOUS) // <2>
    @View("home") // <3>
    @Get // <4>
    fun index(): Map<String, Any> = HashMap()
}
