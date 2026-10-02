from java.util import Optional
from jakarta.inject import Singleton

from .user_fetcher import UserFetcher
from .user_jdbc_repository import UserJdbcRepository
from .user_state import UserState


@Singleton  # <1>
class UserFetcherService(UserFetcher):
    def __init__(self, user_jdbc_repository: UserJdbcRepository):  # <2>
        self.user_jdbc_repository = user_jdbc_repository

    def findByUsername(self, username: str) -> Optional[UserState]:
        return self.user_jdbc_repository.findByUsername(username)
