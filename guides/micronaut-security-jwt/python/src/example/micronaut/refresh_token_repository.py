from typing import Annotated

from jakarta.transaction import Transactional
from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.core.annotation import NonNull
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository
from java.util import Optional

from .refresh_token_entity import RefreshTokenEntity


# tag::clazz[]
@JdbcRepository(dialect=Dialect.H2)  # <1>
class RefreshTokenRepository(CrudRepository[RefreshTokenEntity, int]):  # <2>
    @Transactional
    def save(
        self,
        username: Annotated[str, NonNull, NotBlank],
        refreshToken: Annotated[str, NonNull, NotBlank],
        revoked: Annotated[bool, NonNull, NotNull],
    ) -> RefreshTokenEntity: ...  # <3>

    def findByRefreshToken(
        self,
        refreshToken: Annotated[str, NonNull, NotBlank],
    ) -> Optional[RefreshTokenEntity]: ...  # <4>

    def updateByUsername(
        self,
        username: Annotated[str, NonNull, NotBlank],
        revoked: bool,
    ) -> int: ...  # <5>
# end::clazz[]
