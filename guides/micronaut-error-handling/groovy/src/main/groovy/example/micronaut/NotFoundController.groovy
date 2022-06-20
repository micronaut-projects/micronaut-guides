package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.hateoas.Link
import io.micronaut.views.ViewsRenderer

@CompileStatic
@Controller('/notfound') // <1>
class NotFoundController {

    private final ViewsRenderer viewsRenderer

    NotFoundController(ViewsRenderer viewsRenderer) { // <2>
        this.viewsRenderer = viewsRenderer
    }

    @Error(status = HttpStatus.NOT_FOUND, global = true)  // <3>
    HttpResponse notFound(HttpRequest request) {
        if (request.headers.accept().find {it.name.contains(MediaType.TEXT_HTML) }) { // <4>
            return HttpResponse.ok(viewsRenderer.render('notFound', Collections.emptyMap(), request))
                    .contentType(MediaType.TEXT_HTML)
        }

        JsonError error = new JsonError('Page Not Found')
                .link(Link.SELF, Link.of(request.uri))

        return HttpResponse.<JsonError>notFound()
                .body(error) // <5>
    }
}
