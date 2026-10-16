from com.nimbusds.jose import JWSAlgorithm
from java.security.interfaces import RSAPublicKey
from com.nimbusds.jose.jwk import JWK, RSAKey
from micronaut.context.exceptions import ConfigurationException
from micronaut.security.token.jwt.signature.rsa import RSASignatureConfiguration


# tag::clazz[]
class AbstractRSASignatureConfiguration(RSASignatureConfiguration):  # <1>
    def __init__(self, json_jwk: str):
        rsa_key = self._parse_rsa_key(json_jwk)
        if rsa_key is None:
            raise ConfigurationException("could not parse primary JWK to RSA Key")

        self.public_jwk = rsa_key.toPublicJWK()
        self.private_key = rsa_key.toRSAPrivateKey()
        self.public_key = rsa_key.toRSAPublicKey()

        self.jws_algorithm = self._parse_jws_algorithm(rsa_key)
        if self.jws_algorithm is None:
            raise ConfigurationException("could not parse JWS Algorithm from RSA Key")

    def getPublicJWK(self) -> JWK:
        return self.public_jwk

    def getPublicKey(self) -> RSAPublicKey:
        return self.public_key

    def _parse_jws_algorithm(self, rsa_key: RSAKey):
        algorithm = rsa_key.getAlgorithm()
        if algorithm is None:
            return None
        if isinstance(algorithm, JWSAlgorithm):
            return algorithm
        return JWSAlgorithm.parse(algorithm.getName())

    def _parse_rsa_key(self, json_jwk: str):
        jwk = JWK.parse(json_jwk)
        if not isinstance(jwk, RSAKey):
            return None
        return jwk
# end::clazz[]
