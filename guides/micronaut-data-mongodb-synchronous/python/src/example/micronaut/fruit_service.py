from abc import ABC, abstractmethod
from typing import List

from .fruit import Fruit


class FruitService(ABC):
    @abstractmethod
    def list(self) -> List[Fruit]:
        pass

    @abstractmethod
    def save(self, fruit: Fruit) -> Fruit:
        pass

    @abstractmethod
    def find(self, id: str) -> Fruit | None:
        pass

    @abstractmethod
    def find_by_name_in_list(self, names: List[str]) -> List[Fruit]:
        pass
