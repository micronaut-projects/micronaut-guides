from dataclasses import dataclass

from micronaut.data.annotation import Embeddable
from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable
@Embeddable  # <1>
class UserRoleId:
    userId: int  # <2>
    roleId: int  # <2>
