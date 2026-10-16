import pytest
from micronaut.security.token.jwt.validator import JsonWebTokenParser, JsonWebTokenSignatureValidator
from micronaut.security.authentication import UsernamePasswordCredentials
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))
    yield fixture
    fixture.stop()


@pytest.fixture
def app_client(my_context):
    return my_context["example.micronaut.AppClient"]  # <1>


def test_verify_jwt_authentication_works_with_declarative_client(app_client, my_context):
    creds = UsernamePasswordCredentials("sherlock", "password")
    login_rsp = app_client.login(creds)  # <2>

    assert login_rsp is not None
    assert login_rsp.getAccessToken() is not None
    parsed = my_context[JsonWebTokenParser].parse(login_rsp.getAccessToken())
    assert parsed.isPresent()
    assert my_context[JsonWebTokenSignatureValidator].validateSignature(parsed.get())

    msg = app_client.home(f"Bearer {login_rsp.getAccessToken()}")  # <3>
    assert msg == "sherlock"
