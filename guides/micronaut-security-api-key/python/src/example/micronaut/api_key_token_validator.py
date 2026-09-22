from jakarta.inject import Singleton
from micronaut.core.async_.publisher import Publishers
from micronaut.security.authentication import Authentication
from micronaut.security.token.validator import TokenValidator
from org.reactivestreams import Publisher

from .api_key_repository import ApiKeyRepository


# tag::clazz[]
@Singleton  # <1>
class ApiKeyTokenValidator(TokenValidator):
    def __init__(self, api_key_repository: ApiKeyRepository):  # <2>
        self.api_key_repository = api_key_repository

    def validateToken(self, token: str, request) -> Publisher:
        if request is None or not request.getPath().startswith("/api"):  # <3>
            return Publishers.empty()
        name = self.api_key_repository.find_by_api_key(token)
        if name is None:
            return Publishers.empty()
        return Publishers.just(Authentication.build(name))
# end::clazz[]
