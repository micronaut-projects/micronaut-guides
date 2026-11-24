from abc import ABC, abstractmethod
from typing import Annotated

from jakarta.validation.constraints import NotBlank

from .contact import Contact


class ContactRepository(ABC):

    @abstractmethod
    def find_all(self) -> list[Contact]:
        ...

    @abstractmethod
    def find_by_first_name(self, first_name: Annotated[str, NotBlank]) -> Contact | None:  # <1>
        ...
