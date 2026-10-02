from jakarta.inject import Singleton
from micronaut.http import HttpRequest
from micronaut.security.authentication import (
    AuthenticationFailureReason,
    AuthenticationRequest,
    AuthenticationResponse,
)
from micronaut.security.authentication.provider import HttpRequestAuthenticationProvider

from .authorities_fetcher import AuthoritiesFetcher
from .password_encoder import PasswordEncoder
from .user_fetcher import UserFetcher
from .user_state import UserState


@Singleton  # <1>
class DelegatingAuthenticationProvider(HttpRequestAuthenticationProvider):
    def __init__(
        self,
        user_fetcher: UserFetcher,
        password_encoder: PasswordEncoder,
        authorities_fetcher: AuthoritiesFetcher,
    ):
        self.user_fetcher = user_fetcher
        self.password_encoder = password_encoder
        self.authorities_fetcher = authorities_fetcher

    def authenticate(
        self,
        http_request: HttpRequest,
        authentication_request: AuthenticationRequest,
    ) -> AuthenticationResponse:
        user = self.fetch_user_state(authentication_request)  # <2>
        failure = self.validate(user, authentication_request)
        if failure is not None:
            return AuthenticationResponse.failure(failure)
        return self.create_successful_authentication_response(user)  # <3>

    def validate(self, user: UserState | None, authentication_request):
        if user is None:
            return AuthenticationFailureReason.USER_NOT_FOUND
        if not user.enabled:
            return AuthenticationFailureReason.USER_DISABLED
        if user.accountExpired:
            return AuthenticationFailureReason.ACCOUNT_EXPIRED
        if user.accountLocked:
            return AuthenticationFailureReason.ACCOUNT_LOCKED
        if user.passwordExpired:
            return AuthenticationFailureReason.PASSWORD_EXPIRED
        if not self.password_encoder.matches(
            str(authentication_request.getSecret()),
            user.password,
        ):
            return AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH
        return None

    def fetch_user_state(self, authentication_request):
        username = str(authentication_request.getIdentity())
        return self.user_fetcher.findByUsername(username).orElse(None)

    def create_successful_authentication_response(
        self,
        user: UserState,
    ) -> AuthenticationResponse:
        authorities = self.authorities_fetcher.findAuthoritiesByUsername(user.username)
        return AuthenticationResponse.success(user.username, authorities)
