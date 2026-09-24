from java.util import Collections
from com.nimbusds.jwt import JWTParser, SignedJWT
from pyronaut.test import MicronautTest, micronaut_test_fixture
import pytest

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


def test_can_generate_signed_json_web_tokens(my_context):
    token_generator = my_context["io.micronaut.security.token.generator.TokenGenerator"]
    token_validator = my_context[
        "io.micronaut.security.token.jwt.validator.JsonWebTokenValidator"
    ]

    jwt_optional = token_generator.generateToken(Collections.singletonMap("sub", "sergio"))
    assert jwt_optional.isPresent()

    jwt_string = jwt_optional.get()
    jwt = JWTParser.parse(jwt_string)
    assert isinstance(jwt, SignedJWT)

    token_validator.validate(jwt_string, None)

    claims = jwt.getJWTClaimsSet()
    assert claims.getClaims().keySet().size() == 1
    assert claims.getSubject() == "sergio"
