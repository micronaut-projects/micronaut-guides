from jakarta.inject import Singleton
from micronaut.runtime.server.event import ServerStartupEvent
from micronaut.runtime.event.annotation import EventListener

from .register_service import RegisterService


@Singleton
class Application:  # <1>
    def __init__(self, register_service: RegisterService):  # <2>
        self.register_service = register_service

    @EventListener
    def onApplicationEvent(self, event: ServerStartupEvent) -> None:  # <1>
        self.register_service.register(
            "sherlock@micronaut.example",
            "sherlock",
            "elementary",
            ["ROLE_DETECTIVE"],
        )  # <3>
