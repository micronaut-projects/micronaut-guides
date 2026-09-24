from com.nimbusds.jose import JWSAlgorithm
from java.security.interfaces import RSAPrivateKey
from micronaut.security.token.jwt.signature.rsa import RSASignatureGeneratorConfiguration

from .abstract_rsa_signature_configuration import AbstractRSASignatureConfiguration


# tag::clazz[]
class AbstractRSAGeneratorSignatureConfiguration(
    AbstractRSASignatureConfiguration,
    RSASignatureGeneratorConfiguration,
):  # <1>
    def getPrivateKey(self) -> RSAPrivateKey:
        return self.private_key

    def getJwsAlgorithm(self) -> JWSAlgorithm:
        return self.jws_algorithm
# end::clazz[]
