import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.message_service import MessageService


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def service(my_context):
    return my_context[MessageService]  # <2>


def test_it_works(service):
    assert service.say_hello("Tim") == "Hello Tim!"


def test_validation_with_blank(service):
    with pytest.raises(BaseException) as exc:
        service.say_hello("   ")

    assert "say_hello.name: must not be blank" in str(exc.value)
