import pytest
import requests
from micronaut.email import AsyncTransactionalEmailSender, BodyType
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.test_email_sender_replacement import EmailSenderReplacement


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={"spec.name": "MailControllerTest"},  # <1>
            transactional=False,
        ),  # <2>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <3>


def test_mail_send_endpoint_sends_an_email(client, my_context):
    response = client.post("/mail/send", json={"to": "johnsnow@micronaut.example"})
    assert response.status_code == 202

    sender = my_context[AsyncTransactionalEmailSender]
    assert isinstance(sender, EmailSenderReplacement)
    assert len(sender.emails) == 1

    email = sender.emails[0]
    assert email.getFrom().getEmail() == "john@micronaut.example"
    assert email.getTo().stream().findFirst().get().getEmail() == "johnsnow@micronaut.example"
    assert email.getSubject() == "Sending email with Twilio Sendgrid is Fun"
    assert email.getBody().get(BodyType.HTML).get() == (
        "and <em>easy</em> to do anywhere with <strong>Micronaut Email</strong>"
    )
