from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get

from example.micronaut.message_service import MessageService


@Controller("/field")
class MessageController:
    message_service: Annotated[MessageService, Inject]

    @Get(produces=MediaType.TEXT_PLAIN)
    def index(self) -> str:
        return self.message_service.compose()
