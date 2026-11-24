from jakarta.validation import Validator

from example.micronaut.contact import Contact


def test_first_name_cannot_be_blank(my_context):
    validator = my_context.getBean(Validator)  # <1>
    assert len(validator.validate(Contact("", "del Amo", None))) > 0  # <2>
    assert len(validator.validate(Contact(None, "del Amo", None))) > 0  # <3>
