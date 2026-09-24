from abc import ABC, abstractmethod

from micronaut.core.order import Ordered
from micronaut.http import HttpRequest


class ColorFetcher(Ordered, ABC):  # <1> <2>
    @abstractmethod
    def favourite_color(self, request: HttpRequest) -> str | None:
        ...
