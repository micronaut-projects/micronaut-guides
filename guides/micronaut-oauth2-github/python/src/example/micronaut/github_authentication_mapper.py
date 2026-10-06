from jakarta.inject import Named, Singleton
from micronaut.security.authentication import AuthenticationResponse
from micronaut.security.oauth2.endpoint.authorization.state import State
from micronaut.security.oauth2.endpoint.token.response import OauthAuthenticationMapper, TokenResponse
from org.reactivestreams import Publisher
from reactor.core.publisher import Mono

from .github_api_client import GithubApiClient


@Named("github")  # <1>
@Singleton
class GithubAuthenticationMapper(OauthAuthenticationMapper):
    TOKEN_PREFIX = "token "
    ROLE_GITHUB = "ROLE_GITHUB"

    def __init__(self, api_client: GithubApiClient):
        self.api_client = api_client

    def createAuthenticationResponse(
        self,
        tokenResponse: TokenResponse,
        state: State,
    ) -> Publisher:
        access_token = tokenResponse.getAccessToken()

        def success(user):
            return AuthenticationResponse.success(
                user.login,
                [self.ROLE_GITHUB],
                {OauthAuthenticationMapper.ACCESS_TOKEN_KEY: access_token},  # <3>
            )

        return Mono.from_(self.api_client.get_user(self.TOKEN_PREFIX + access_token)).map(success)  # <2>
