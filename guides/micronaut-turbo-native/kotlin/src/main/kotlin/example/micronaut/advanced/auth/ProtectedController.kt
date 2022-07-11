package example.micronaut.advanced.auth

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import example.micronaut.model.ViewModel
import io.micronaut.views.ModelAndView
import java.security.Principal

@Controller("/protected")
class ProtectedController {

    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    fun index(principal: Principal?): HttpResponse<*> {
        if (principal == null) {
            return HttpResponse.status<Any>(HttpStatus.UNAUTHORIZED).body("Unauthorized")
        } else {
            return HttpResponse.ok(ModelAndView("protected", ViewModel("Protected Webpage")))
        }
    }
}