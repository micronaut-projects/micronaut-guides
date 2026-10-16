from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import Embeddable, Relation

from .role import Role
from .user_entity import UserEntity


@dataclass
@Embeddable  # <1>
class UserRoleId:
    user: Annotated[UserEntity, Relation(value="MANY_TO_ONE")]  # <2>
    role: Annotated[Role, Relation(value="MANY_TO_ONE")]  # <2>
