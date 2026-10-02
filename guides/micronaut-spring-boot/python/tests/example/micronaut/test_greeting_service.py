import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False, transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


def test_spring_configuration_properties(my_context):
    greeting_configuration = my_context["example.micronaut.GreetingConfiguration"]

    assert greeting_configuration.template == "Hola, %s!"


def test_regex_validation_non_digits_work(my_context):
    greeting_service = my_context["example.micronaut.GreetingService"]

    greeting_service.greeting("foo")
    with pytest.raises(BaseException) as exc:
        greeting_service.greeting("12foo")

    assert "ConstraintViolationException" in str(exc.value)
