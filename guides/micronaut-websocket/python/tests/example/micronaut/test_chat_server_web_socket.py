from abc import ABC, abstractmethod
from time import sleep

import pytest
from micronaut.context.annotation import Requires
from micronaut.runtime.server import EmbeddedServer
from micronaut.websocket import WebSocketClient
from micronaut.websocket.annotation import ClientWebSocket, OnMessage
from pyronaut.test import MicronautTest, micronaut_test_fixture
from reactor.core.publisher import Flux


@Requires(property="spec.name", value="ChatWebSocketTest")  # <1>
@ClientWebSocket("/ws/chat/{topic}/{username}")  # <3>
class TestWebSocketClient(ABC):  # <4>

    def __init__(self):
        self.message_history: list[str] = []

    @property
    def latest_message(self) -> str | None:
        return self.message_history[-1] if self.message_history else None

    @property
    def messages_chronologically(self) -> list[str]:
        return list(self.message_history)

    @OnMessage  # <5>
    def on_message(self, message: str) -> None:
        self.message_history.append(message)

    def send(self, message: str) -> None:  # <6>
        ...

    @abstractmethod
    def close(self) -> None:
        ...


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            transactional=False,
            properties={"spec.name": "ChatWebSocketTest"},
        ),
    )  # <2>
    yield fixture
    fixture.stop()


@pytest.fixture
def websocket_client(my_context):
    server = my_context[EmbeddedServer]
    client = server.getApplicationContext().createBean(WebSocketClient, server.getURI())
    yield client
    client.close()


def create_websocket_client(websocket_client, username: str, topic: str):
    return Flux.from_(
        websocket_client.connect(
            TestWebSocketClient,
            {"topic": topic, "username": username},
        )
    ).blockFirst()


def messages(client) -> list[str]:
    return client.messages_chronologically


def await_condition(condition) -> None:  # <7>
    for _ in range(50):
        if condition():
            return
        sleep(0.1)
    assert condition()


def test_websocket_server(websocket_client):
    clients = []
    try:
        adam = create_websocket_client(websocket_client, "adam", "Cats & Recreation")  # <8>
        clients.append(adam)
        await_condition(lambda: messages(adam) == ["[adam] Joined Cats & Recreation!"])  # <9>

        anna = create_websocket_client(websocket_client, "anna", "Cats & Recreation")
        clients.append(anna)
        await_condition(lambda: messages(anna) == ["[anna] Joined Cats & Recreation!"])
        await_condition(
            lambda: messages(adam)
            == ["[adam] Joined Cats & Recreation!", "[anna] Joined Cats & Recreation!"]
        )

        ben = create_websocket_client(websocket_client, "ben", "Fortran Tips & Tricks")
        clients.append(ben)
        await_condition(lambda: messages(ben) == ["[ben] Joined Fortran Tips & Tricks!"])

        zach = create_websocket_client(websocket_client, "zach", "all")
        clients.append(zach)
        await_condition(lambda: messages(zach) == ["[zach] Now making announcements!"])

        cienna = create_websocket_client(websocket_client, "cienna", "Fortran Tips & Tricks")
        clients.append(cienna)
        await_condition(lambda: messages(cienna) == ["[cienna] Joined Fortran Tips & Tricks!"])
        await_condition(
            lambda: messages(ben)
            == [
                "[ben] Joined Fortran Tips & Tricks!",
                "[zach] Now making announcements!",
                "[cienna] Joined Fortran Tips & Tricks!",
            ]
        )  # <10>

        adams_greeting = "Hello, everyone. It's another purrrfect day :-)"
        expected_greeting = f"[adam] {adams_greeting}"
        adam.send(adams_greeting)  # <11>

        await_condition(lambda: adam.latest_message == expected_greeting)
        await_condition(lambda: anna.latest_message == expected_greeting)
        assert ben.latest_message != expected_greeting
        await_condition(lambda: zach.latest_message == expected_greeting)
        assert cienna.latest_message != expected_greeting

        anna.close()  # <12>

        anna_leaving = "[anna] Leaving Cats & Recreation!"
        await_condition(lambda: adam.latest_message == anna_leaving)
        assert adam.latest_message == anna_leaving
        assert anna.latest_message != anna_leaving
        assert ben.latest_message != anna_leaving
        assert zach.latest_message == anna_leaving
        assert cienna.latest_message != anna_leaving
    finally:
        for client in clients:
            client.close()
