from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import EmbeddedId, MappedEntity
from micronaut.serde.annotation import Serdeable

from .user_role_id import UserRoleId


@dataclass
@Serdeable
@MappedEntity  # <1>
class UserRole:
    userRoleId: Annotated[UserRoleId, EmbeddedId]  # <2>
