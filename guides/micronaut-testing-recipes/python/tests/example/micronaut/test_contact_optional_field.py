from jakarta.validation import Validator

from example.micronaut.contact import Contact


def test_company_can_be_null(my_context):
    validator = my_context.getBean(Validator)  # <1>
    assert len(validator.validate(Contact("Sergio", "del Amo", None))) == 0  # <2>
