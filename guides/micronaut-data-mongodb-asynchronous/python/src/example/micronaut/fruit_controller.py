from typing import Annotated, List

from jakarta.validation import Valid
from jakarta.validation.constraints import NotNull
from micronaut.core.async_.annotation import SingleResult
from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Controller, Get, Post, Put, QueryValue, Status
from org.reactivestreams import Publisher

from .fruit import Fruit
from .fruit_service import FruitService


@Controller("/fruits")  # <1>
class FruitController:
    def __init__(self, fruit_service: FruitService):  # <2>
        self.fruit_service = fruit_service

    @Get  # <3>
    def list(self) -> Publisher[Fruit]:
        return self.fruit_service.list()

    @Post  # <4>
    @Status(HttpStatus.CREATED)  # <5>
    @SingleResult
    def save(self, fruit: Annotated[Fruit, Body, NotNull, Valid]) -> Publisher[Fruit]:  # <6>
        return self.fruit_service.save(fruit)

    @Put
    @SingleResult
    def update(self, fruit: Annotated[Fruit, Body, NotNull, Valid]) -> Publisher[Fruit]:
        return self.fruit_service.save(fruit)

    @Get("/{id}")  # <7>
    def find(self, id: str) -> Publisher[Fruit]:
        return self.fruit_service.find(id)

    @Get("/q")  # <8>
    def query(self, names: Annotated[List[str], QueryValue, NotNull]) -> Publisher[Fruit]:  # <9>
        return self.fruit_service.findByNameInList(names)
