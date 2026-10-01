import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.contact_entity import ContactEntity
from example.micronaut.phone_entity import PhoneEntity


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False, transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def contact_repository(my_context):
    return my_context["example.micronaut.ContactRepository"]  # <2>


@pytest.fixture
def phone_repository(my_context):
    return my_context["example.micronaut.PhoneRepository"]  # <3>


def test_associations_querying(contact_repository, phone_repository):
    first_name = "Sergio"
    last_name = "del Amo"
    contact_count = contact_repository.count()
    contact = contact_repository.save(ContactEntity(None, first_name, last_name))
    assert contact_repository.count() == contact_count + 1

    preview = contact_repository.findPreviewById(contact.id).orElse(None)
    assert preview is not None
    assert preview.id == contact.id
    assert preview.firstName == first_name
    assert preview.lastName == last_name

    contact_with_join = contact_repository.getById(contact.id).orElse(None)
    assert contact_with_join is not None
    assert contact_with_join.id == contact.id
    assert contact_with_join.firstName == first_name
    assert contact_with_join.lastName == last_name
    assert list(contact_with_join.phones) == []

    complete = contact_repository.findCompleteById(contact.id).orElse(None)
    assert complete is not None
    assert complete.id == contact.id
    assert complete.firstName == first_name
    assert complete.lastName == last_name
    assert complete.phones is None

    american_phone = "+14155552671"
    uk_phone = "+442071838750"
    phone_count = phone_repository.count()
    contact_reference = ContactEntity(contact.id, first_name, last_name)
    us_phone = phone_repository.save(PhoneEntity(None, american_phone, contact_reference))
    uk_phone_entity = phone_repository.save(PhoneEntity(None, uk_phone, contact_reference))
    assert phone_repository.count() == phone_count + 2

    preview = contact_repository.findPreviewById(contact.id).orElse(None)
    assert preview is not None
    assert preview.id == contact.id
    assert preview.firstName == first_name
    assert preview.lastName == last_name

    contact_without_join = contact_repository.findById(contact.id).orElse(None)
    assert contact_without_join is not None
    assert list(contact_without_join.phones) == []

    contact_with_join = contact_repository.getById(contact.id).orElse(None)
    assert contact_with_join is not None
    phones = list(contact_with_join.phones)
    assert {phone.phone for phone in phones} == {american_phone, uk_phone}
    assert {phone.id for phone in phones} == {us_phone.id, uk_phone_entity.id}

    complete = contact_repository.findCompleteById(contact.id).orElse(None)
    assert complete is not None
    assert set(complete.phones) == {american_phone, uk_phone}

    phone_repository.deleteById(us_phone.id)
    phone_repository.deleteById(uk_phone_entity.id)
    contact_repository.deleteById(contact.id)
    assert phone_repository.count() == phone_count
    assert contact_repository.count() == contact_count
