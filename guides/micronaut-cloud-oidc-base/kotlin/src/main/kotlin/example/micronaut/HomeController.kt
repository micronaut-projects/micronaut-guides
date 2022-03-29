package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.micronaut.views.View

@Controller // <1>
class HomeController {

    @Secured(IS_ANONYMOUS) // <2>
    @View("home") // <3>
    @Get // <4>
    fun index(): Map<String, Any> = mapOf()

    @Secured(IS_AUTHENTICATED) // <5>
    @Get("/secure") // <6>
    fun secured(): Map<String, Any> = mapOf("secured" to true) // <7>
}
