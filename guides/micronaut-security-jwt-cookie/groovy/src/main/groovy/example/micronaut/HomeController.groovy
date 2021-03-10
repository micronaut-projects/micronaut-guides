package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.views.View

import javax.annotation.Nullable
import java.security.Principal

@CompileStatic
@Secured("isAnonymous()") // <1>
@Controller("/") // <2>
class HomeController {

    @Get("/") // <3>
    @View("home") // <4>
    Map<String, Object> index(@Nullable Principal principal) { // <5>
        Map<String, Object> data = [:]
        data["loggedIn"] = principal != null
        if (principal) {
            data["username"] = principal.name
        }
        data
    }
}
