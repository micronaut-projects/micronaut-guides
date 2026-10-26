from java.net import URI
from micronaut.http import HttpResponse
from micronaut.http.annotation import Controller, Get
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule


@Controller  # <1>
class HomeController:

    @Secured(SecurityRule.IS_ANONYMOUS)  # <2>
    @Get  # <3>
    def index(self) -> HttpResponse:
        return HttpResponse.seeOther(URI("/contacts"))  # <4>
