from collections import deque
from time import monotonic, sleep

import pytest
import requests
from jakarta.inject import Singleton
from micronaut.configuration.kafka.annotation import KafkaListener, OffsetReset, Topic
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.book import Book


@Singleton
class ReceivedMessages:
    def __init__(self):
        self.messages = deque()

    def append(self, book: Book):
        self.messages.append(book)

    def clear(self):
        self.messages.clear()

    def __len__(self):
        return len(self.messages)

    def __getitem__(self, index: int):
        return self.messages[index]


@KafkaListener(offsetReset=OffsetReset.EARLIEST)  # <8>
class AnalyticsListener:

    def __init__(self, received_messages: ReceivedMessages):
        self.received_messages = received_messages

    @Topic("analytics")
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
    return requests.with_context(my_context)  # <3>


@pytest.fixture
def received_messages(my_context):
    messages = my_context["example.micronaut.ReceivedMessages"]
    yield messages
    messages.clear()


def wait_for_message(received_messages: ReceivedMessages, timeout: float = 5.0):
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if received_messages:
            return
        sleep(0.1)
    raise AssertionError("Expected Kafka message was not received")


def test_message_is_published_to_kafka_when_book_found(client, received_messages):
    isbn = "1491950358"

    response = client.get(f"/books/{isbn}")  # <4>

    assert response.status_code == 200
    assert response.json()["isbn"] == isbn

    wait_for_message(received_messages)  # <5>

    assert len(received_messages) == 1  # <6>
    assert received_messages[0].isbn == isbn


def test_message_is_not_published_to_kafka_when_book_not_found(client, received_messages):
    response = client.get("/books/INVALID")

    assert response.status_code == 404
    sleep(5.0)  # <7>
    assert len(received_messages) == 0
