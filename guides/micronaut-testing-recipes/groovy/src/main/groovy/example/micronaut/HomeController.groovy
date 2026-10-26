package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@CompileStatic
@Controller // <1>
class HomeController {

    private static final URI CONTACTS = URI.create("/contacts")

    @Secured(SecurityRule.IS_ANONYMOUS) // <2>
    @Get // <3>
    HttpResponse<?> index() {
        HttpResponse.seeOther(CONTACTS) // <4>
    }
}
