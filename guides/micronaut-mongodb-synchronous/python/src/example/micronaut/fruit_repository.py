from abc import ABC, abstractmethod
from typing import Annotated

from jakarta.validation import Valid

from .fruit import Fruit


class FruitRepository(ABC):

    @abstractmethod
    def list(self) -> list[Fruit]:
        pass

    @abstractmethod
    def save(self, fruit: Annotated[Fruit, Valid]) -> None:  # <1>
        pass
