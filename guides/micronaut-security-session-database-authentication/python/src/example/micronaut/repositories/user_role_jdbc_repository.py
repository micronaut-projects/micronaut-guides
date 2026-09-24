from micronaut.data.annotation import Query
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from ..entities.user_role import UserRole
from ..entities.user_role_id import UserRoleId


@JdbcRepository(dialect="POSTGRES")  # <1>
class UserRoleJdbcRepository(CrudRepository[UserRole, UserRoleId]):  # <2>
    @Query(value="""
        SELECT role_.authority
        FROM role role_
        INNER JOIN user_role user_role_ ON user_role_.role_id = role_.id
        INNER JOIN "user" user_ ON user_role_.user_id = user_.id
        WHERE user_.username = :username
        """)  # <3>
    def findAllAuthoritiesByUsername(self, username: str) -> list[str]: ...
