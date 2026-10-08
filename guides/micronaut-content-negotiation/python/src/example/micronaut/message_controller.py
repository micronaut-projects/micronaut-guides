from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.views import View


@Controller  # <1>
class MessageController:
    @Get(produces=MediaType.APPLICATION_JSON)  # <2>
    def json_message(self) -> dict[str, str]:  # <3>
        return {"message": "Hello World"}

    @View("message.html")  # <4>
    @Get(produces=MediaType.TEXT_HTML)
    def html_message(self) -> dict[str, str]:
        return {"message": "Hello World"}
