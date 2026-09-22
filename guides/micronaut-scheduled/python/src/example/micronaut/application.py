import time

from jakarta.inject import Singleton
from micronaut.runtime.server.event import ServerStartupEvent
from micronaut.runtime.event.annotation import EventListener

from .register_use_case import RegisterUseCase


@Singleton  # <1>
class Application:  # <2>
    def __init__(self, register_use_case: RegisterUseCase):  # <3>
        self.register_use_case = register_use_case

    @EventListener
    def onApplicationEvent(self, event: ServerStartupEvent) -> None:  # <4>
        self.register_use_case.register("harry@micronaut.example")
        time.sleep(20)
        self.register_use_case.register("ron@micronaut.example")
