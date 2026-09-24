from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from ..entities.user import User


@JdbcRepository(dialect="POSTGRES")  # <1>
class UserJdbcRepository(CrudRepository[User, int]):  # <2>
    def findByUsername(self, username: str) -> Optional[User]: ...
