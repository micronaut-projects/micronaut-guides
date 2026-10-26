from abc import ABC, abstractmethod


class Calculator(ABC):

    @abstractmethod
    def add(self, a: int, b: int) -> int:
        ...
