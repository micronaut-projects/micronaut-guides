from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity
from micronaut.serde.annotation import Serdeable

from ..user_state import UserState


@dataclass
@Serdeable
@MappedEntity  # <1>
class User(UserState):
    username: Annotated[str, NotBlank]  # <2>
    password: Annotated[str, NotBlank]
    enabled: bool
    accountExpired: bool
    accountLocked: bool
    passwordExpired: bool
    id: Annotated[int | None, Id, GeneratedValue] = None  # <3> <4>
