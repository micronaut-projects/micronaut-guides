import java

from micronaut.http import HttpRequest, HttpResponse, MediaType
from micronaut.http.annotation import Controller, Get, Produces

ModelAndView = java.type("io.micronaut.views.ModelAndView")


@Controller  # <1>
class MessageController:

    @Produces([MediaType.TEXT_HTML, MediaType.APPLICATION_JSON])  # <2>
    @Get  # <3>
    def index(self, request: HttpRequest) -> HttpResponse:  # <4>
        model = {"message": "Hello World"}
        body = (
            ModelAndView("message.html", model)
            if accepts(request, MediaType.TEXT_HTML)
            else model
        )
        return HttpResponse.ok(body)


def accepts(request: HttpRequest, media_type: str) -> bool:
    return any(
        media_type in accepted.getName()
        for accepted in request.getHeaders().accept()
    )
