import java

from micronaut.http import HttpResponse
from micronaut.http.annotation import Controller, Get
from micronaut.http.uri import UriBuilder


Hidden = java.type("io.swagger.v3.oas.annotations.Hidden")


@Controller  # <1>
class HomeController:

    ADOC = UriBuilder.of("/swagger").path("micronaut-guides-1.0.adoc").build()

    @Get  # <2>
    @Hidden  # <3>
    def home(self) -> HttpResponse:
        return HttpResponse.seeOther(self.ADOC)
