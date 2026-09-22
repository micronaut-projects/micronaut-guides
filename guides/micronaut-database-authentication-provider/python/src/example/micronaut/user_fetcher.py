from java.util import Optional

from .user_state import UserState


class UserFetcher:
    def findByUsername(self, username: str) -> Optional[UserState]: ...
