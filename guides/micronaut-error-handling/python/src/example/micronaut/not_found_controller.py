from micronaut.http import HttpRequest, HttpResponse, HttpStatus, MediaType
from micronaut.http.annotation import Controller, Error
from micronaut.http.hateoas import JsonError, Link
from micronaut.views import ViewsRenderer


# tag::clazz[]
@Controller("/notfound")  # <1>
class NotFoundController:
    def __init__(self, views_renderer: ViewsRenderer):  # <2>
        self.views_renderer = views_renderer

    @Error(status=HttpStatus.NOT_FOUND, global_=True)  # <3>
    def not_found(self, request: HttpRequest) -> HttpResponse:
        if any(
            MediaType.TEXT_HTML in accepted.getName()
            for accepted in request.getHeaders().accept()
        ):  # <4>
            return HttpResponse.ok(
                self.views_renderer.render("notFound", {}, request)
            ).header("Content-Type", MediaType.TEXT_HTML)

        error = JsonError("Page Not Found").link(Link.SELF, Link.of(request.getUri()))
        return HttpResponse.status(HttpStatus.NOT_FOUND).body(error)  # <5>
# end::clazz[]
