from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.http.annotation import Controller, Get
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from ..domain.thing import Thing
from ..repository.thing_repository import ThingRepository


@Controller("/things")
@ExecuteOn(TaskExecutors.BLOCKING)
class ThingController:
    def __init__(self, thing_repository: ThingRepository):
        self.thing_repository = thing_repository

    @Get
    def all(self) -> list[Thing]:
        return list(self.thing_repository.findAll())

    @Get("/{name}")
    def by_name(self, name: Annotated[str, NotBlank]) -> Thing | None:
        return self.thing_repository.findByName(name).orElse(None)
