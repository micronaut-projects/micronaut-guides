package example.micronaut

import io.micronaut.security.token.jwt.signature.rsa.RSASignatureGeneratorConfiguration

abstract class AbstractRSAGeneratorSignatureConfiguration(jsonJwk: String) :
        AbstractRSASignatureConfiguration(jsonJwk), RSASignatureGeneratorConfiguration // <1>
