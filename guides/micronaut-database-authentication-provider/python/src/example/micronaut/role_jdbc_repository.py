from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .domain.role import Role


@JdbcRepository(dialect="H2")  # <1>
class RoleJdbcRepository(CrudRepository[Role, int]):  # <2>
    def save(self, authority: str) -> Role: ...

    def findByAuthority(self, authority: str) -> Optional[Role]: ...
