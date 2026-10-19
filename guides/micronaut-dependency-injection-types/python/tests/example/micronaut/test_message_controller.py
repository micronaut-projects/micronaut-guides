import pytest

from pyronaut import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.constructor.message_controller import MessageController
from example.micronaut.message_service import MessageService


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:
        yield session


@pytest.mark.parametrize("path", ["/constructor", "/field", "/setter"])
def test_message_controllers(client, path: str):
    response = client.get(path, headers={"Accept": "text/plain"})

    assert response.status_code == 200
    assert response.text == "Hello World"


# tag::constructorUnitTest[]
def test_constructor_injection_without_application_context():
    controller = MessageController(MessageService())

    assert controller.index() == "Hello World"
# end::constructorUnitTest[]
