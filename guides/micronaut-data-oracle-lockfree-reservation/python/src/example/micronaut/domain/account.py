from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, PositiveOrZero
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity, Reservable
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@MappedEntity("ACCOUNT")  # <2>
@dataclass
class Account:
    id: Annotated[int | None, Id, GeneratedValue]  # <3>
    name: Annotated[str, NotBlank]  # <4>
    balance: Annotated[int, Reservable, PositiveOrZero]  # <5>
    credit: Annotated[int, Reservable, PositiveOrZero]  # <6>
