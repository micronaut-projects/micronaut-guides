import pytest

from micronaut.email.javamail.sender import DefaultSessionProvider, SessionProvider
from micronaut.email.javamail.sender.authentication import JavaMailAuthenticationConfiguration
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def auth_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=False,
            properties={
                "javamail.authentication.username": "fake-smtp-user",
                "javamail.authentication.password": "fake-smtp-password",
                "javamail.properties.mail.smtp.host": "smtp.invalid",
            },
        ),
    )
    yield fixture
    fixture.stop()


def test_builtin_session_provider_uses_authentication_configuration(auth_context):
    configuration = auth_context[JavaMailAuthenticationConfiguration]
    assert configuration.isEnabled()
    assert configuration.getUsername() == "fake-smtp-user"
    assert configuration.getPassword() == "fake-smtp-password"
    assert isinstance(auth_context[SessionProvider], DefaultSessionProvider)
