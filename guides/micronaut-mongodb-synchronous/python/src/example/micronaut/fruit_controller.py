from typing import Annotated

from jakarta.validation import Valid
from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Controller, Get, Post, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .fruit import Fruit
from .fruit_repository import FruitRepository


@Controller("/fruits")  # <1>
@ExecuteOn(TaskExecutors.BLOCKING)  # <2>
class FruitController:

    def __init__(self, fruit_repository: FruitRepository):  # <3>
        self.fruit_repository = fruit_repository

    @Get  # <4>
    def list(self) -> list[Fruit]:
        return self.fruit_repository.list()

    @Post  # <5>
    @Status(HttpStatus.CREATED)  # <6>
    def save(self, fruit: Annotated[Fruit, Body, Valid]) -> None:  # <7>
        self.fruit_repository.save(fruit)
