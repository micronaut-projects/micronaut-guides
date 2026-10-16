from jakarta.inject import Named, Singleton
from micronaut.http import HttpHeaderValues
from micronaut.security.authentication import AuthenticationResponse
from micronaut.security.oauth2.endpoint.authorization.state import State
from micronaut.security.oauth2.endpoint.token.response import OauthAuthenticationMapper, TokenResponse
from org.reactivestreams import Publisher
from reactor.core.publisher import Mono

from .linkedin_api_client import LinkedInApiClient


@Named("linkedin")  # <1>
@Singleton  # <2>
class LinkedInOauthAuthenticationMapper(OauthAuthenticationMapper):

    def __init__(self, linked_in_api_client: LinkedInApiClient):  # <3>
        self.linked_in_api_client = linked_in_api_client

    def createAuthenticationResponse(
        self,
        tokenResponse: TokenResponse,
        state: State,
    ) -> Publisher:
        authorization = f"{HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} {tokenResponse.getAccessToken()}"

        def success(linked_in_me):
            attributes = {
                "firstName": linked_in_me.localizedFirstName,
                "lastName": linked_in_me.localizedLastName,
            }
            return AuthenticationResponse.success(linked_in_me.id, [], attributes)

        return Mono.from_(self.linked_in_api_client.me(authorization)).map(success)
