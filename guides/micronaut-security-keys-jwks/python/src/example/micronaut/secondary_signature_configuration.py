from jakarta.inject import Named
from micronaut.runtime.context.scope import Refreshable

from .abstract_rsa_signature_configuration import AbstractRSASignatureConfiguration
from .jwk_configuration import JwkConfiguration


# tag::clazz[]
@Named("secondary")
@Refreshable  # <1>
class SecondarySignatureConfiguration(AbstractRSASignatureConfiguration):
    def __init__(self, jwk_configuration: JwkConfiguration):
        super().__init__(jwk_configuration.secondary)
# end::clazz[]
