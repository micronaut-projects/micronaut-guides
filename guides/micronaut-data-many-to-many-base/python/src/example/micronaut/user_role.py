from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import EmbeddedId, MappedEntity

from .user_role_id import UserRoleId


@dataclass
@MappedEntity  # <1>
class UserRole:
    id: Annotated[UserRoleId, EmbeddedId]  # <2>
