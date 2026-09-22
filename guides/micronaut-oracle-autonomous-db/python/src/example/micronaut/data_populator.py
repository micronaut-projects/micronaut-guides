from jakarta.inject import Singleton
from jakarta.transaction import Transactional
from micronaut.context.annotation import Requires
from micronaut.context.env import Environment
from micronaut.context.event import StartupEvent
from micronaut.runtime.event.annotation import EventListener

from .domain.thing import Thing
from .repository.thing_repository import ThingRepository


@Singleton
@Requires(notEnv=Environment.TEST)
class DataPopulator:
    def __init__(self, thing_repository: ThingRepository):
        self.thing_repository = thing_repository

    @EventListener
    @Transactional
    def init(self, event: StartupEvent) -> None:
        self.thing_repository.deleteAll()
        self.thing_repository.save(Thing("Fred"))
        self.thing_repository.save(Thing("Barney"))
