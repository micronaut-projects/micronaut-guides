package example.micronaut.advanced

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import example.micronaut.model.ViewModel
import io.micronaut.views.View

@Controller("/success")
class SuccessController {

    @View("success")
    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    ViewModel index() {
        new ViewModel("It Worked!")
    }
}