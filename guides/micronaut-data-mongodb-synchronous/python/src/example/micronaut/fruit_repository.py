from typing import List

from micronaut.data.mongodb.annotation import MongoRepository
from micronaut.data.repository import CrudRepository

from .fruit import Fruit


@MongoRepository  # <1>
class FruitRepository(CrudRepository[Fruit, str]):
    def findByNameInList(self, names: List[str]) -> List[Fruit]: ...  # <2>
