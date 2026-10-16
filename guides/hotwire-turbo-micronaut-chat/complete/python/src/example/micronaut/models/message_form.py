from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.serde.annotation import Serdeable


# tag::clazz[]
@dataclass
@Serdeable  # <1>
class MessageForm:
    room: Annotated[int, NotNull]
    content: Annotated[str, NotBlank]
# end::clazz[]
