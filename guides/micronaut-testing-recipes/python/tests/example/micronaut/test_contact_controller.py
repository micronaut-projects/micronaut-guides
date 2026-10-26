import pytest
import requests
from jakarta.inject import Singleton
from micronaut.context.annotation import Requires, Replaces
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.contact import Contact
from example.micronaut.contact_repository import ContactRepository


@Requires(property="spec.name", value="ContactControllerTest")  # <1>
@Replaces(ContactRepository)  # <2>
@Singleton
class ContactRepositoryReplacement(ContactRepository):
    def find_all(self) -> list[Contact]:
        return [
            Contact("Katherine", "Adams", "Apple Inc."),
            Contact("Tim", "Cook", "Apple Inc."),
        ]

    def find_by_first_name(self, first_name: str) -> Contact | None:
        return next(
            (contact for contact in self.find_all() if contact.firstName == first_name),
            None,
        )


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"spec.name": "ContactControllerTest"},
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <3>


def test_contacts_are_rendered_as_html(client):
    response = client.get("/contacts", headers={"Accept": "text/html"})  # <4>
    assert response.status_code == 200  # <5>
    assert "<!DOCTYPE html>" in response.text
    assert "Katherine" in response.text
    assert "Adams" in response.text
    assert "Tim" in response.text
    assert "Cook" in response.text
