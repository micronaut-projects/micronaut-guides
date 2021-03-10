package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.views.View
import java.security.Principal

@Secured("isAnonymous()") // <1>
@Controller("/") // <2>
class HomeController {

    @Get("/") // <3>
    @View("home")// <4>
    fun index(principal: Principal?): Map<String, Any> { // <5>
        val data = mutableMapOf<String, Any>()
        data["loggedIn"] = (principal != null) as Any
        if (principal != null) {
            data["username"] = principal.name as Any
        }
        return data
    }
}
