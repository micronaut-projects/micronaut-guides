from typing import List

from jakarta.inject import Singleton
from org.reactivestreams import Publisher

from .fruit import Fruit
from .fruit_repository import FruitRepository
from .fruit_service import FruitService


@Singleton  # <1>
class DefaultFruitService(FruitService):
    def __init__(self, fruit_repository: FruitRepository):
        self.fruit_repository = fruit_repository

    def list(self) -> Publisher[Fruit]:
        return self.fruit_repository.findAll()

    def save(self, fruit: Fruit) -> Publisher[Fruit]:
        if fruit.id is None:
            return self.fruit_repository.save(fruit)
        return self.fruit_repository.update(fruit)

    def find(self, id: str) -> Publisher[Fruit]:
        return self.fruit_repository.findById(id)

    def findByNameInList(self, names: List[str]) -> Publisher[Fruit]:
        return self.fruit_repository.findByNameInList(names)
