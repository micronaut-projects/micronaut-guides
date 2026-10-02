from typing import Annotated

from jakarta.inject import Inject
from jakarta.validation import Valid
from jakarta.validation.constraints import NotNull
from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Get, Post, Put, QueryValue, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .fruit import Fruit
from .fruit_service import FruitService

fruit_service: Annotated[FruitService, Inject]  # <1>


@ExecuteOn(TaskExecutors.BLOCKING)  # <2>
@Get("/fruits")  # <3>
def list_fruits() -> list[Fruit]:
    return fruit_service.list()


@ExecuteOn(TaskExecutors.BLOCKING)
@Post("/fruits")  # <4>
@Status(HttpStatus.CREATED)  # <5>
def save(fruit: Annotated[Fruit, Body, Valid]) -> Fruit:  # <6>
    return fruit_service.save(fruit)


@ExecuteOn(TaskExecutors.BLOCKING)
@Put("/fruits")
def update(fruit: Annotated[Fruit, Body, Valid]) -> Fruit:
    return fruit_service.save(fruit)


@ExecuteOn(TaskExecutors.BLOCKING)
@Get("/fruits/{id}")  # <7>
def find(id: str) -> Fruit | None:
    return fruit_service.find(id)


@ExecuteOn(TaskExecutors.BLOCKING)
@Get("/fruits/q")  # <8>
def query(names: Annotated[list[str], QueryValue, NotNull]) -> list[Fruit]:  # <9>
    return fruit_service.find_by_name_in_list(names)
