from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.views import View


@Controller  # <1>
class HomeController:

    @View("index.html")  # <3>
    @Get(produces=MediaType.TEXT_HTML)  # <2>
    def index(self) -> dict[str, object]:
        return {}
