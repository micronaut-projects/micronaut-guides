from jakarta.inject import Singleton
from micronaut.security.authentication import (
    AuthenticationFailureReason,
    AuthenticationResponse,
)
from micronaut.security.authentication.provider import HttpRequestAuthenticationProvider


# tag::clazz[]
@Singleton  # <1>
class AuthenticationProviderUserPassword(HttpRequestAuthenticationProvider):  # <2>
    def authenticate(self, http_request, authentication_request) -> AuthenticationResponse:
        identity = authentication_request.getIdentity()
        secret = authentication_request.getSecret()
        if identity == "sherlock" and secret == "password":
            return AuthenticationResponse.success(identity)
        return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
# end::clazz[]
