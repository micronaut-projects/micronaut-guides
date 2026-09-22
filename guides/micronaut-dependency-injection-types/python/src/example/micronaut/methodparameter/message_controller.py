from jakarta.inject import Inject
from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get

from example.micronaut.message_service import MessageService


@Controller("/setter")
class MessageController:
    def __init__(self):
        self.message_service: MessageService | None = None

    @Get(produces=MediaType.TEXT_PLAIN)
    def index(self) -> str:
        assert self.message_service is not None
        return self.message_service.compose()

    @Inject
    def populate_message_service(self, message_service: MessageService):
        self.message_service = message_service
