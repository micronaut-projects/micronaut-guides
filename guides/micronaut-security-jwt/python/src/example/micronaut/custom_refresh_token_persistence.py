from jakarta.inject import Singleton
from micronaut.security.authentication import Authentication
from micronaut.security.errors import (
    IssuingAnAccessTokenErrorCode,
    OauthErrorResponseException,
)
from micronaut.security.token.refresh import RefreshTokenPersistence
from org.reactivestreams import Publisher
from reactor.core.publisher import Flux, FluxSink

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

    def getAuthentication(self, refreshToken: str) -> Publisher:
        def emit(emitter):
            token_optional = self.refresh_token_repository.findByRefreshToken(refreshToken)
            if token_optional.isPresent():
                token = token_optional.get()
                if token.revoked:
                    emitter.error(
                        OauthErrorResponseException(
                            IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                            "refresh token revoked",
                            None,
                        )
                    )  # <5>
                else:
                    emitter.next(Authentication.build(token.username))  # <6>
                    emitter.complete()
            else:
                emitter.error(
                    OauthErrorResponseException(
                        IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "refresh token not found",
                        None,
                    )
                )  # <7>

        return Flux.create(emit, FluxSink.OverflowStrategy.ERROR)
# end::clazz[]
