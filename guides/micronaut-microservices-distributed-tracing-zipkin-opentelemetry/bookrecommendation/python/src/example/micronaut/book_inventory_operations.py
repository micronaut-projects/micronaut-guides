from abc import ABC, abstractmethod
from typing import Annotated

from jakarta.validation.constraints import NotBlank


class BookInventoryOperations(ABC):

    @abstractmethod
    async def stock(self, isbn: Annotated[str, NotBlank]) -> bool | None:
        ...
