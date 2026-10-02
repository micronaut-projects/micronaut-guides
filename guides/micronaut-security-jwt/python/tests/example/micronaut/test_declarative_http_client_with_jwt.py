import pytest
from com.nimbusds.jwt import JWTParser, SignedJWT
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


def test_verify_jwt_authentication_works_with_declarative_client(app_client):
    creds = UsernamePasswordCredentials("sherlock", "password")
    login_rsp = app_client.login(creds)  # <2>

    assert login_rsp is not None
    assert login_rsp.getAccessToken() is not None
    assert isinstance(JWTParser.parse(login_rsp.getAccessToken()), SignedJWT)

    msg = app_client.home(f"Bearer {login_rsp.getAccessToken()}")  # <3>
    assert msg == "sherlock"
