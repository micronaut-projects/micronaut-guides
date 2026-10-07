from typing import Annotated

from micronaut.data.annotation import Id
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .domain.account import Account


@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
class AccountRepository(CrudRepository[Account, int]):  # <2>

    def reserveIncrementBalance(self, id: Annotated[int, Id], balance: int) -> int: ...  # <3>

    def reserveDecrementBalance(self, id: Annotated[int, Id], balance: int) -> int: ...  # <4>
