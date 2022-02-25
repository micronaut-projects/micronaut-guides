package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.hateoas.Link
import io.micronaut.views.ViewsRenderer

@Controller("/notfound") // <1>
class NotFoundController(private val viewsRenderer: ViewsRenderer<Any>) { // <2>

    @Error(status = HttpStatus.NOT_FOUND, global = true) // <3>
    fun notFound(request: HttpRequest<*>): HttpResponse<*> {
        if (request.headers.accept().any { it.name.contains(MediaType.TEXT_HTML) }) { // <4>
            return HttpResponse.ok(viewsRenderer.render("notFound", emptyMap<Any, Any>(), request))
                    .contentType(MediaType.TEXT_HTML)
        }

        val error = JsonError("Page Not Found")
                .link(Link.SELF, Link.of(request.uri))

        return HttpResponse.notFound<JsonError>()
                .body(error) // <5>
    }
}
