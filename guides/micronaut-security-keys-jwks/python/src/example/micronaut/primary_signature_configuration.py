from jakarta.inject import Named
from micronaut.runtime.context.scope import Refreshable

from .abstract_rsa_generator_signature_configuration import AbstractRSAGeneratorSignatureConfiguration
from .jwk_configuration import JwkConfiguration


# tag::clazz[]
@Refreshable  # <1>
@Named("generator")  # <2>
class PrimarySignatureConfiguration(AbstractRSAGeneratorSignatureConfiguration):
    def __init__(self, jwk_configuration: JwkConfiguration):
        super().__init__(jwk_configuration.primary)
# end::clazz[]
