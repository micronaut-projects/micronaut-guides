package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.views.View

@CompileStatic
@Secured("isAnonymous()") // <1>
@Controller("/login") // <2>
class LoginAuthController {

    @Get("/auth") // <3>
    @View("auth") // <4>
    Map<String, Object> auth() {
        [:]
    }

    @Get("/authFailed") // <5>
    @View("auth") // <4>
    Map<String, Object> authFailed() {
        [errors: true] as Map<String, Object>
    }
}
