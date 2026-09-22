from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.security.oauth2.client import OpenIdProviderMetadata
from micronaut.security.oauth2.configuration import OauthClientConfiguration
from micronaut.security.oauth2.endpoint.token.response import OpenIdClaims
from micronaut.security.oauth2.endpoint.token.response.validation import OpenIdClaimsValidator

from .application_configuration import ApplicationConfiguration


@Requires(beans=ApplicationConfiguration)
@Singleton
class HostedDomainClaimValidator(OpenIdClaimsValidator):
    HOSTED_DOMAIN_CLAIM = "hd"

    def __init__(self, application_configuration: ApplicationConfiguration):
        self.hosted_domain = application_configuration.hosted_domain

    def validate(
        self,
        claims: OpenIdClaims,
        clientConfiguration: OauthClientConfiguration,
        providerMetadata: OpenIdProviderMetadata,
    ) -> bool:
        hosted_domain = claims.get(self.HOSTED_DOMAIN_CLAIM)
        return isinstance(hosted_domain, str) and hosted_domain.lower() == self.hosted_domain.lower()
