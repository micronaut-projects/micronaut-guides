from typing import Annotated

import java
from jakarta.transaction import Transactional
from jakarta.validation.constraints import NotBlank
from micronaut.data.annotation import Id
from micronaut.data.exceptions import DataAccessException
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.r2dbc.annotation import R2dbcRepository
from micronaut.data.repository.reactive import ReactorPageableRepository
from org.reactivestreams import Publisher
from reactor.core.publisher import Mono

from .domain.genre import Genre


@R2dbcRepository(dialect=Dialect.MYSQL)  # <1>
class GenreRepository(ReactorPageableRepository[Genre, int]):  # <2>
    def save(self, name: Annotated[str, NotBlank]) -> Publisher[Genre]: ...

    @Transactional
    def saveWithException(
        self,
        name: Annotated[str, NotBlank],
    ) -> Publisher[Genre]:
        return Mono.from_(self.save(name)).then(Mono.error(DataAccessException("test exception")))

    def update(
        self,
        id: Annotated[int, Id],
        name: Annotated[str, NotBlank],
    ) -> Publisher[int]: ...
