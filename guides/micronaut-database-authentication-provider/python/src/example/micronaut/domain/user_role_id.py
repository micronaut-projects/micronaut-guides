from dataclasses import dataclass

from micronaut.data.annotation import Embeddable
from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable  # <1>
@Embeddable  # <2>
class UserRoleId:  # <3>
    userId: int
    roleId: int
