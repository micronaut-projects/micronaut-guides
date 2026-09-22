from typing import List

from micronaut.data.mongodb.annotation import MongoRepository
from micronaut.data.repository.reactive import ReactiveStreamsCrudRepository
from org.reactivestreams import Publisher

from .fruit import Fruit


@MongoRepository  # <1>
class FruitRepository(ReactiveStreamsCrudRepository[Fruit, str]):  # <2>
    def findByNameInList(self, names: List[str]) -> Publisher[Fruit]: ...  # <3>
