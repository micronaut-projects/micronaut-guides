from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get
from micronaut.views import View

from .message_service import MessageService


@Controller  # <1>
class MainController:
    def __init__(self, message_service: MessageService):  # <2>
        self.message_service = message_service

    @View("index.html")  # <3>
    @Get(value="/hello/{name}", produces=MediaType.TEXT_HTML)  # <4>
    def index(self, name: Annotated[str, NotBlank]) -> dict[str, str]:  # <5>
        return {"message": self.message_service.say_hello(name)}
