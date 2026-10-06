from dataclasses import dataclass
from datetime import datetime
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.core.annotation import NonNull
from micronaut.data.annotation import DateCreated, GeneratedValue, Id, MappedEntity


# tag::clazzwithoutsettersandgetters[]
@dataclass
@MappedEntity  # <1>
class RefreshTokenEntity:
    id: Annotated[int | None, Id, GeneratedValue, NonNull] = None  # <2> <3>
    username: Annotated[str | None, NonNull, NotBlank] = None
    refreshToken: Annotated[str | None, NonNull, NotBlank] = None
    revoked: Annotated[bool | None, NonNull, NotNull] = None
    date_created: Annotated[datetime | None, DateCreated, NonNull, NotNull] = None  # <4>
# end::clazzwithoutsettersandgetters[]
# tag::endclass[]
# end::endclass[]
