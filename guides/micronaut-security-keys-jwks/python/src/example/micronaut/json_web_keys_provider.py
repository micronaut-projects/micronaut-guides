from java.util import Arrays, List
from micronaut.runtime.context.scope import Refreshable
from micronaut.security.token.jwt.endpoints import JwkProvider

from .primary_signature_configuration import PrimarySignatureConfiguration
from .secondary_signature_configuration import SecondarySignatureConfiguration


# tag::clazz[]
@Refreshable  # <1>
class JsonWebKeysProvider(JwkProvider):  # <2>
    def __init__(
        self,
        primary_rsa_private_key: PrimarySignatureConfiguration,
        secondary_signature_configuration: SecondarySignatureConfiguration,
    ):
        self.jwks = Arrays.asList(
            primary_rsa_private_key.getPublicJWK(),
            secondary_signature_configuration.getPublicJWK(),
        )

    def retrieveJsonWebKeys(self) -> List:
        return self.jwks
# end::clazz[]
