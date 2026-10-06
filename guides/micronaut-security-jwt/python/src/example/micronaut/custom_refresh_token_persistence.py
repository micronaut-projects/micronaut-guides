from jakarta.inject import Singleton
from micronaut.security.authentication import Authentication
from micronaut.security.errors import (
    IssuingAnAccessTokenErrorCode,
    OauthErrorResponseException,
)
from micronaut.security.token.refresh import RefreshTokenPersistence

from .refresh_token_repository import RefreshTokenRepository


# tag::clazz[]
@Singleton  # <1>
class CustomRefreshTokenPersistence(RefreshTokenPersistence):
    def __init__(self, refresh_token_repository: RefreshTokenRepository):  # <2>
        self.refresh_token_repository = refresh_token_repository

    def persistToken(self, event) -> None:  # <3>
        if (
            event is not None
            and event.getRefreshToken() is not None
            and event.getAuthentication() is not None
            and event.getAuthentication().getName() is not None
        ):
            payload = event.getRefreshToken()
            self.refresh_token_repository.save(event.getAuthentication().getName(), payload, False)  # <4>

    async def getAuthentication(self, refresh_token: str) -> Authentication:
        token = self.refresh_token_repository.findByRefreshToken(refresh_token)
        if token is None:
            raise OauthErrorResponseException(
                IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                "refresh token not found",
                None,
            )  # <7>
        if token.revoked:
            raise OauthErrorResponseException(
                IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                "refresh token revoked",
                None,
            )  # <5>
        return Authentication.build(token.username)  # <6>
# end::clazz[]
