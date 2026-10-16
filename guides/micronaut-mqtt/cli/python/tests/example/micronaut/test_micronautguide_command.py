from decimal import Decimal
from time import monotonic, sleep

import pytest
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.micronautguide_command import MicronautguideCommand
from example.micronaut.temperature_listener import TemperatureListener


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={"spec.name": "MicronautguideCommandTest"},
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def command(my_context) -> MicronautguideCommand:
    return my_context[MicronautguideCommand]


@pytest.fixture
def listener(my_context) -> TemperatureListener:
    return my_context[TemperatureListener]


def wait_for_temperature(listener: TemperatureListener, expected: Decimal, timeout: float = 5.0) -> None:
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if listener.temperature == expected:
            return
        sleep(0.1)
    raise AssertionError(f"Expected {expected} but got {listener.temperature}")


def test_with_command_line_option(command, listener, capsys):
    command.run(Decimal("212"), "Fahrenheit")

    assert "Topic published" in capsys.readouterr().out
    wait_for_temperature(listener, Decimal("100.00"))
