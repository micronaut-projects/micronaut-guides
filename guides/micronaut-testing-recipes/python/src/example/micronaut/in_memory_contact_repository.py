from typing import Annotated

from jakarta.inject import Singleton
from jakarta.validation.constraints import NotBlank
from micronaut.validation import Validated

from .contact import Contact
from .contact_repository import ContactRepository


@Validated
@Singleton  # <1>
class InMemoryContactRepository(ContactRepository):
    def __init__(self):
        self.contacts = [
            Contact("Sergio", "del Amo", "Object Computing"),
            Contact("Graeme", "Rocher", "Oracle"),
        ]

    def find_all(self) -> list[Contact]:
        return self.contacts

    def find_by_first_name(self, first_name: Annotated[str, NotBlank]) -> Contact | None:
        return next(
            (contact for contact in self.contacts if contact.firstName == first_name),
            None,
        )
