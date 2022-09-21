package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.views.View

import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED

@CompileStatic
@Controller // <1>
class HomeController {

    @Secured(IS_ANONYMOUS) // <2>
    @View('home') // <3>
    @Get // <4>
    Map<String, Object> index() {
        [:]
    }

    @Secured(IS_AUTHENTICATED) // <5>
    @Get('/secure') // <6>
    Map<String, Object> secured() {
        [secured: true] as Map // <7>
    }
}
