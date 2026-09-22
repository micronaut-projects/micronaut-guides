from collections import deque
from time import monotonic, sleep

import pytest
import requests
from jakarta.inject import Singleton
from micronaut.rabbitmq.annotation import Queue, RabbitListener
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.analytics_client import AnalyticsClient
from example.micronaut.book import Book


@Singleton
class ReceivedMessages:
    def __init__(self):
        self.messages = deque()

    def append(self, book: Book):
        self.messages.append(book)

    def clear(self):
        self.messages.clear()

    def count(self) -> int:
        return len(self.messages)

    def first_isbn(self) -> str | None:
        if self.messages:
            return self.messages[0].isbn
        return None


@RabbitListener  # <8>
class AnalyticsTestListener:

    def __init__(self, received_messages: ReceivedMessages):
        self.received_messages = received_messages

    @Queue("analytics")
    def update_analytics(self, book: Book):
        self.received_messages.append(book)


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


@pytest.fixture
def received_messages(my_context):
    messages = my_context["example.micronaut.ReceivedMessages"]
    yield messages
    messages.clear()


@pytest.fixture
def analytics_client(my_context):
    return my_context[AnalyticsClient]


def wait_for_message(received_messages: ReceivedMessages, timeout: float = 5.0):
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if received_messages.count() > 0:
            return
        sleep(0.1)
    raise AssertionError("Expected RabbitMQ message was not received")


def wait_for_rabbitmq(analytics_client: AnalyticsClient, received_messages: ReceivedMessages):
    deadline = monotonic() + 10.0
    last_error = None
    while monotonic() < deadline:
        try:
            analytics_client.update_analytics(Book("READY", "RabbitMQ Ready"))
            wait_for_message(received_messages)
            received_messages.clear()
            return
        except BaseException as e:
            last_error = e
            sleep(0.25)
    raise AssertionError("RabbitMQ did not become ready") from last_error


def test_message_is_published_to_rabbitmq_when_book_found(client, analytics_client, received_messages):
    isbn = "1491950358"
    wait_for_rabbitmq(analytics_client, received_messages)

    response = client.get(f"/books/{isbn}")

    assert response.status_code == 200
    assert response.json()["isbn"] == isbn

    wait_for_message(received_messages)

    assert received_messages.count() == 1
    assert received_messages.first_isbn() == isbn


def test_message_is_not_published_to_rabbitmq_when_book_not_found(client, received_messages):
    response = client.get("/books/INVALID")

    assert response.status_code == 404
    sleep(1.0)
    assert received_messages.count() == 0
