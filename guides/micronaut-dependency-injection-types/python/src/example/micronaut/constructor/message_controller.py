from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get

from example.micronaut.message_service import MessageService


@Controller("/constructor")
class MessageController:
    def __init__(self, message_service: MessageService):
        self.message_service = message_service

    @Get(produces=MediaType.TEXT_PLAIN)
    def index(self) -> str:
        return self.message_service.compose()
