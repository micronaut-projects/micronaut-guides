package example.micronaut.advanced

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.net.URI

@Controller("/follow")
class FollowController {

    @Get(produces = [MediaType.TEXT_HTML], consumes = [MediaType.TEXT_HTML])
    fun index() = HttpResponse.temporaryRedirect<Any>(URI.create("/redirected"))
}