from typing import List

from org.reactivestreams import Publisher

from .fruit import Fruit


class FruitService:
    def list(self) -> Publisher[Fruit]: ...

    def save(self, fruit: Fruit) -> Publisher[Fruit]: ...

    def find(self, id: str) -> Publisher[Fruit]: ...

    def findByNameInList(self, names: List[str]) -> Publisher[Fruit]: ...
