package example.micronaut.basic

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import example.micronaut.model.ViewModel
import io.micronaut.views.View

@Controller("/scroll")
class ScrollController {

    @View("scroll")
    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    fun index() = ViewModel("Restoring Your Scroll")
}