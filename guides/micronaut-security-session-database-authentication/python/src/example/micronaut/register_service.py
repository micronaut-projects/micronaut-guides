from jakarta.inject import Singleton
from jakarta.transaction import Transactional

from .entities.role import Role
from .entities.user import User
from .entities.user_role import UserRole
from .entities.user_role_id import UserRoleId
from .exceptions.user_already_exists_exception import UserAlreadyExistsException
from .password_encoder import PasswordEncoder
from .repositories.role_jdbc_repository import RoleJdbcRepository
from .repositories.user_jdbc_repository import UserJdbcRepository
from .repositories.user_role_jdbc_repository import UserRoleJdbcRepository


@Singleton  # <1>
class RegisterService:
    def __init__(
        self,
        role_jdbc_repository: RoleJdbcRepository,
        user_jdbc_repository: UserJdbcRepository,
        password_encoder: PasswordEncoder,
        user_role_jdbc_repository: UserRoleJdbcRepository,
    ):
        self.role_jdbc_repository = role_jdbc_repository
        self.user_jdbc_repository = user_jdbc_repository
        self.password_encoder = password_encoder
        self.user_role_jdbc_repository = user_role_jdbc_repository

    @Transactional  # <2>
    def register(
        self,
        username: str,
        raw_password: str,
        authorities: list[str],
    ) -> None:
        if self.user_jdbc_repository.findByUsername(username).isPresent():
            raise UserAlreadyExistsException()

        encoded_password = self.password_encoder.encode(raw_password)
        user = self.user_jdbc_repository.save(
            User(
                username=username,
                password=encoded_password,
                enabled=True,
                accountExpired=False,
                accountLocked=False,
                passwordExpired=False,
            )
        )

        for authority in authorities:
            role = self.find_or_save_role(authority)
            user_role_id = UserRoleId(user.id, role.id)
            if self.user_role_jdbc_repository.findById(user_role_id).isEmpty():
                self.user_role_jdbc_repository.save(UserRole(user_role_id))

    def find_or_save_role(self, authority: str) -> Role:
        role = self.role_jdbc_repository.findByAuthority(authority)
        if role.isPresent():
            return role.get()
        return self.role_jdbc_repository.save(authority)
