from micronaut.http import HttpRequest, HttpResponse, MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.views import ModelAndView


@Controller  # <1>
class MessageController:
    @Get(produces=[MediaType.TEXT_HTML, MediaType.APPLICATION_JSON])  # <2> <3>
    def index(self, request: HttpRequest) -> HttpResponse:  # <4>
        model = {"message": "Hello World"}
        if prefers_html(request):
            return HttpResponse.ok(ModelAndView("message", model))
        return HttpResponse.ok(model).contentType(MediaType.APPLICATION_JSON)


def prefers_html(request: HttpRequest) -> bool:
    for accepted in request.getHeaders().accept():  # ordered by quality
        name = str(accepted.getName())
        if name == MediaType.TEXT_HTML:
            return True
        if name == MediaType.APPLICATION_JSON:
            return False
    return False
