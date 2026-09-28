from java.util import Optional
from micronaut.data.annotation import Query
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .user import User
from .user_entity import UserEntity


@JdbcRepository(dialect="ORACLE")  # <1>
class UserJdbcRepository(CrudRepository[UserEntity, int]):  # <2>
    def save(self, username: str) -> UserEntity: ...

    # <3>
    @Query(value="""
        SELECT
            u.id,
            u.username,
            LISTAGG(r.authority, ',') WITHIN GROUP (ORDER BY r.authority) AS authorities
        FROM users u
        LEFT JOIN user_role ur ON ur.user_id = u.id
        LEFT JOIN role r ON r.id = ur.role_id
        WHERE u.username = :username
        GROUP BY u.id, u.username
        """)
    def findByUsername(self, username: str) -> Optional[User]: ...
