from typing import Annotated

from jakarta.validation import Valid
from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Controller, Get, Post, Put, Status

from .fruit import Fruit
from .fruit_service import FruitService


@Controller("/fruits")  # <1>
class FruitController:
    def __init__(self, fruit_service: FruitService):  # <2>
        self.fruit_service = fruit_service

    @Get  # <3>
    def list_fruits(self) -> list[Fruit]:
        return self.fruit_service.list_fruits()

    @Post  # <4>
    @Status(HttpStatus.CREATED)  # <5>
    def save(self, fruit: Annotated[Fruit, Body, Valid]) -> Fruit:  # <6>
        return self.fruit_service.save(fruit)

    @Put
    def update(self, fruit: Annotated[Fruit, Body, Valid]) -> Fruit:
        return self.fruit_service.save(fruit)

    @Get("/{id}")  # <7>
    def find(self, id: str) -> Fruit | None:
        return self.fruit_service.find(id)

    @Get("/q")  # <8>
    def query(self, names: list[str]) -> list[Fruit]:  # <9>
        return self.fruit_service.find_by_name_in_list(names)
