package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

import java.security.Principal

@CompileStatic
@Secured(SecurityRule.IS_ANONYMOUS) // <1>
@Controller // <2>
class HomeController {

    @Get // <3>
    @View("home") // <4>
    Map<String, Object> index(@Nullable Principal principal) { // <5>
        Map<String, Object> data = [loggedIn: principal != null] as Map
        if (principal) {
            data.username = principal.name
        }
        data
    }
}
