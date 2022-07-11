package example.micronaut.basic

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import example.micronaut.model.ViewModel
import io.micronaut.views.View

@Controller("/two")
class TwoController {

    @View("two")
    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    fun index(@QueryValue action: String?) = ViewModel("Push or Replace?", action = action)
}