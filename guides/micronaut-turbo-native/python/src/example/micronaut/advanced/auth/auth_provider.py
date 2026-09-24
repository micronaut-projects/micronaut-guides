from jakarta.inject import Singleton
from micronaut.security.authentication import AuthenticationResponse
from micronaut.security.authentication.provider import HttpRequestAuthenticationProvider


@Singleton
class AuthProvider(HttpRequestAuthenticationProvider):
    def authenticate(self, http_request, authentication_request) -> AuthenticationResponse:
        return AuthenticationResponse.success(authentication_request.getIdentity())
