import pytest

from jakarta.validation import Validator
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.contact import Contact
from example.micronaut.e164 import MESSAGE_TEMPLATE


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def validator(my_context):
    return my_context[Validator]  # <2>


def test_contact_validation(validator):
    assert not list(validator.validateValue(Contact, "phone", "+14155552671"))

    violations = list(validator.validateValue(Contact, "phone", "+1-4155552671"))
    assert violations
    assert any(
        violation.getMessageTemplate() == MESSAGE_TEMPLATE
        and violation.getInvalidValue() == "+1-4155552671"
        and violation.getMessage() == "must be a phone in E.164 format"
        for violation in violations
    )
