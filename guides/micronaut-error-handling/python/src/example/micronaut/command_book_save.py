from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, Positive
from micronaut.serde.annotation import Serdeable


# tag::clazz[]
@dataclass
@Serdeable  # <1>
class CommandBookSave:
    title: Annotated[str, NotBlank]  # <2>
    pages: Annotated[int, Positive]  # <3>
# end::clazz[]
