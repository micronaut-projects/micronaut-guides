from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .user_role import UserRole
from .user_role_id import UserRoleId


@JdbcRepository(dialect="MYSQL")  # <1>
class UserRoleJdbcRepository(CrudRepository[UserRole, UserRoleId]):  # <2>
    pass
