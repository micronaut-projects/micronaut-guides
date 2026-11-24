import pytest

from example.micronaut.contact_repository import ContactRepository


def test_find_by_first_name_validates_its_parameter(my_context):
    repository = my_context.getBean(ContactRepository)  # <1>
    with pytest.raises(BaseException) as exc:
        repository.find_by_first_name("")  # <2>
    assert "must not be blank" in str(exc.value)

    with pytest.raises(BaseException) as exc:
        repository.find_by_first_name(None)  # <3>
    assert "must not be blank" in str(exc.value)
