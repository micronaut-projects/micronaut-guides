package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import java.security.Principal

@Secured("isAuthenticated()") // <1>
@Controller("/") // <2>
class HomeController {

    @Produces(MediaType.TEXT_PLAIN) // <3>
    @Get("/")  // <4>
    fun index(principal: Principal): String {  // <5>
        return principal.name
    }
}