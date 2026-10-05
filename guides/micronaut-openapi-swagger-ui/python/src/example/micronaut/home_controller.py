from micronaut.http import HttpResponse
from micronaut.http.annotation import Controller, Get
from micronaut.http.uri import UriBuilder
from swagger.v3.oas.annotations import Hidden


@Controller  # <1>
class HomeController:

    SWAGGER_UI = UriBuilder.of("/swagger-ui").path("index.html").build()

    @Get  # <2>
    @Hidden  # <3>
    def home(self) -> HttpResponse:
        return HttpResponse.seeOther(self.SWAGGER_UI)
