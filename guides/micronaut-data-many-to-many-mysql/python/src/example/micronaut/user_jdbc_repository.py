from java.util import Optional
from micronaut.data.annotation import Query
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .user import User
from .user_entity import UserEntity


@JdbcRepository(dialect="MYSQL")  # <1>
class UserJdbcRepository(CrudRepository[UserEntity, int]):  # <2>
    def save(self, username: str) -> UserEntity: ...

    # <3>
    @Query(value="""
        SELECT
            u.id,
            u.username,
            GROUP_CONCAT(DISTINCT r.authority ORDER BY r.authority SEPARATOR ',') AS authorities
        FROM users AS u
        LEFT JOIN user_role AS ur ON ur.user_id = u.id
        LEFT JOIN role AS r ON r.id = ur.role_id
        WHERE u.username = :username
        GROUP BY u.id, u.username;
        """)
    def findByUsername(self, username: str) -> Optional[User]: ...
