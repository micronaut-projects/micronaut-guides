from abc import ABC, abstractmethod
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from org.reactivestreams import Publisher


class BookInventoryOperations(ABC):

    @abstractmethod
    def stock(self, isbn: Annotated[str, NotBlank]) -> Publisher[bool]:
        ...
