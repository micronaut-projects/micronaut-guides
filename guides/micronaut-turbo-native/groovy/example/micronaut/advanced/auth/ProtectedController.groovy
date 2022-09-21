package example.micronaut.advanced.auth

import io.micronaut.core.annotation.Nullable
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
    HttpResponse<?> index(@Nullable Principal principal) {
        if (principal) {
            HttpResponse.ok(new ModelAndView<>("protected", new ViewModel("Protected Webpage")))
        } else {
            HttpResponse.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")
        }
    }
}