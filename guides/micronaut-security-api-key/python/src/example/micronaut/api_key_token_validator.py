from jakarta.inject import Singleton
from micronaut.security.authentication import Authentication
from micronaut.security.token.validator import TokenValidator

from .api_key_repository import ApiKeyRepository


# tag::clazz[]
@Singleton  # <1>
class ApiKeyTokenValidator(TokenValidator):
    def __init__(self, api_key_repository: ApiKeyRepository):  # <2>
        self.api_key_repository = api_key_repository

    async def validateToken(self, token: str, request) -> Authentication | None:
        if request is None or not request.getPath().startswith("/api"):  # <3>
            return None
        name = self.api_key_repository.find_by_api_key(token)
        if name is None:
            return None
        return Authentication.build(name)
# end::clazz[]
