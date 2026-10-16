from jakarta.inject import Singleton

from .authorities_fetcher import AuthoritiesFetcher
from .user_role_jdbc_repository import UserRoleJdbcRepository


@Singleton  # <1>
class AuthoritiesFetcherService(AuthoritiesFetcher):
    def __init__(self, user_role_jdbc_repository: UserRoleJdbcRepository):  # <2>
        self.user_role_jdbc_repository = user_role_jdbc_repository

    def findAuthoritiesByUsername(self, username: str) -> list[str]:
        return self.user_role_jdbc_repository.findAllAuthoritiesByUsername(username)
