from typing import Annotated

from jakarta.inject import Singleton
from jakarta.validation.constraints import NotBlank
from micronaut.validation import Validated


@Validated
@Singleton  # <1>
class MessageService:
    def say_hello(self, name: Annotated[str, NotBlank]) -> str:
        return f"Hello {name}!"
