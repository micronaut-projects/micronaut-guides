from typing import Annotated

from jakarta.inject import Singleton
from jakarta.transaction import Transactional
from jakarta.validation.constraints import Email, NotBlank

from .domain.role import Role
from .domain.user import User
from .domain.user_role import UserRole
from .domain.user_role_id import UserRoleId
from .password_encoder import PasswordEncoder
from .role_jdbc_repository import RoleJdbcRepository
from .user_jdbc_repository import UserJdbcRepository
from .user_role_jdbc_repository import UserRoleJdbcRepository


@Singleton
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

    @Transactional
    def register(
        self,
        email: Annotated[str, Email],
        username: Annotated[str, NotBlank],
        raw_password: Annotated[str, NotBlank],
        authorities: list[str],
    ) -> None:
        user = self.user_jdbc_repository.findByUsername(username).orElse(None)
        if user is None:
            encoded_password = self.password_encoder.encode(raw_password)
            user = self.user_jdbc_repository.save(
                User(
                    email=email,
                    username=username,
                    password=encoded_password,
                    enabled=True,
                    accountExpired=False,
                    accountLocked=False,
                    passwordExpired=False,
                )
            )

        if user is not None and authorities:
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
