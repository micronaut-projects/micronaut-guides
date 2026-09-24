from java.util import Collections
from com.nimbusds.jwt import JWTParser, SignedJWT
from micronaut.security.token.jwt.generator import JwtTokenGenerator
from micronaut.security.token.jwt.signature.rsa import RSASignatureGenerator
from pyronaut.test import MicronautTest, micronaut_test_fixture
import pytest

from example.micronaut.abstract_rsa_generator_signature_configuration import (
    AbstractRSAGeneratorSignatureConfiguration,
)
from example.micronaut.jwk_configuration import JwkConfiguration
from jwk_test_values import jwk_properties


# <1>
@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=False,
            properties=jwk_properties(),
        ),
    )
    yield fixture
    fixture.stop()


def test_can_validate_a_json_web_token_signed_with_the_secondary_json_web_key(my_context):
    claims_generator = my_context["io.micronaut.security.token.claims.ClaimsGenerator"]
    jwk_configuration = my_context["example.micronaut.JwkConfiguration"]
    token_validator = my_context[
        "io.micronaut.security.token.jwt.validator.JsonWebTokenValidator"
    ]

    signature_generator_configuration = RSASignatureGenerator(
        MockSignatureGenerator(jwk_configuration)
    )
    token_generator = JwtTokenGenerator(
        signature_generator_configuration,
        None,
        claims_generator,
    )

    jwt_optional = token_generator.generateToken(Collections.singletonMap("sub", "sergio"))
    assert jwt_optional.isPresent()

    jwt_string = jwt_optional.get()
    jwt = JWTParser.parse(jwt_string)
    assert isinstance(jwt, SignedJWT)

    token_validator.validate(jwt_string, None)


class MockSignatureGenerator(AbstractRSAGeneratorSignatureConfiguration):
    def __init__(self, jwk_configuration: JwkConfiguration):
        super().__init__(jwk_configuration.secondary)
