from decimal import Decimal
from time import monotonic, sleep

import pytest
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.temperature_client import TemperatureClient
from example.micronaut.temperature_listener import TemperatureListener


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={"spec.name": "SubscriptionTest"},  # <2>
            transactional=False,
        ),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def temperature_client(my_context) -> TemperatureClient:
    return my_context[TemperatureClient]


@pytest.fixture
def temperature_listener(my_context) -> TemperatureListener:
    return my_context[TemperatureListener]


def wait_for_temperature(listener: TemperatureListener, expected: Decimal, timeout: float = 5.0) -> None:
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if listener.temperature == expected:
            return
        sleep(0.1)
    raise AssertionError(f"Expected {expected} but got {listener.temperature}")


def test_subscriptions_are_received(temperature_client, temperature_listener):
    temperature_client.publish_livingroom_temperature(b"3.145")

    wait_for_temperature(temperature_listener, Decimal("3.145"))
