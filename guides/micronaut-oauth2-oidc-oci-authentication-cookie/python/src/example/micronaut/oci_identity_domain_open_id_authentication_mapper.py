from collections.abc import Iterable

from jakarta.inject import Singleton
from micronaut.context.annotation import Replaces
from micronaut.core.async_.publisher import Publishers
from micronaut.security.authentication import AuthenticationResponse
from micronaut.security.oauth2.endpoint.token.response import (
    DefaultOpenIdAuthenticationMapper,
    OpenIdAuthenticationMapper,
    OpenIdClaims,
    OpenIdTokenResponse,
)
from org.reactivestreams import Publisher


@Singleton  # <1>
@Replaces(DefaultOpenIdAuthenticationMapper)
class OciIdentityDomainOpenIdAuthenticationMapper(OpenIdAuthenticationMapper):
    KEY_GROUPS = "groups"
    KEY_NAME = "name"

    def createAuthenticationResponse(
        self,
        providerName: str,
        tokenResponse: OpenIdTokenResponse,
        openIdClaims: OpenIdClaims,
        state,
    ) -> Publisher:
        return Publishers.just(self.authentication_response(openIdClaims))

    def authentication_response(self, open_id_claims: OpenIdClaims) -> AuthenticationResponse:
        roles = self.resolve_roles(open_id_claims)
        return AuthenticationResponse.success(open_id_claims.getSubject(), roles)

    def resolve_roles(self, claims) -> list[str]:
        groups = self._get(claims, self.KEY_GROUPS)
        if groups is None or isinstance(groups, str) or not isinstance(groups, Iterable):
            return []

        roles = []
        for group in groups:
            name = self._get(group, self.KEY_NAME)
            if name is not None and str(name):
                roles.append(str(name))
        return roles

    @staticmethod
    def _get(values, key):
        if values is None:
            return None
        if hasattr(values, "containsKey"):
            return values.get(key) if values.containsKey(key) else None
        if isinstance(values, dict):
            return values.get(key)
        return None
