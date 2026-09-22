import pytest
import requests

from micronaut.email import BodyType, Contact, TransactionalEmailSender
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={"spec.name": "EmailControllerTest"},  # <1>
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def replacement_sender(my_context):
    return my_context[TransactionalEmailSender]


def first_contact(contacts):
    return contacts.iterator().next()


def assert_contact_email(contact, email):
    assert contact.equals(Contact(email))


def assert_template_body(body, view, name):
    model_and_view = body.getModelAndView()
    assert model_and_view.getView().get() == view
    assert model_and_view.getModel().get().get("name") == name


def test_basic_email(client, my_context):
    response = client.post("/email/basic")
    assert response.status_code == 200

    sender = replacement_sender(my_context)
    assert len(sender.emails) == 1
    email = sender.emails[0]

    assert_contact_email(first_contact(email.getTo()), "basic@domain.com")
    assert email.getSubject().startswith("Micronaut Email Basic Test: ")
    assert email.getAttachments() is None
    assert email.getBody().get(BodyType.TEXT).get() == "Basic email"


def test_template_email(client, my_context):
    response = client.post("/email/template/testing")
    assert response.status_code == 200

    sender = replacement_sender(my_context)
    assert len(sender.emails) == 1
    email = sender.emails[0]

    assert_contact_email(first_contact(email.getTo()), "template@domain.com")
    assert email.getSubject().startswith("Micronaut Email Template Test: ")
    assert email.getAttachments() is None
    assert_template_body(email.getBody(), "email", "testing")


def test_attachment_email(client, my_context):
    response = client.post(
        "/email/attachment",
        files={"file": ("test.csv", b"test,email", "text/csv")},
    )
    assert response.status_code == 200

    sender = replacement_sender(my_context)
    assert len(sender.emails) == 1
    email = sender.emails[0]

    assert_contact_email(first_contact(email.getTo()), "attachment@domain.com")
    assert email.getSubject().startswith("Micronaut Email Attachment Test: ")
    assert email.getBody().get(BodyType.TEXT).get() == "Attachment email"

    attachments = email.getAttachments()
    assert attachments is not None
    assert len(attachments) == 1
    attachment = attachments[0]
    assert attachment.getFilename() == "test.csv"
    assert "text/csv" in attachment.getContentType()
    assert bytes(attachment.getContent()).decode("utf-8") == "test,email"
