from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import View


@Controller  # <1>
class HomeController:
    @Secured(SecurityRule.IS_ANONYMOUS)  # <2>
    @View("home")  # <3>
    @Get(produces=MediaType.TEXT_HTML)  # <4>
    def index(self) -> dict[str, object]:
        return {}
