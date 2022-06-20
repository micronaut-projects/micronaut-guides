package example.micronaut.advanced

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import example.micronaut.model.ViewModel
import io.micronaut.views.View
import io.netty.handler.codec.http.HttpHeaderNames

@Controller("/new")
class ModalController {

    @View("new")
    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    fun index() = ViewModel("A Modal Webpage")

    @Post(produces = [MediaType.TEXT_HTML], consumes = [MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_HTML])
    fun post() = HttpResponse.status<Any>(HttpStatus.FOUND).header(HttpHeaderNames.LOCATION, "/success")
}