from time import monotonic, sleep

import java
import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.demo_consumer import DemoConsumer


DockerImageName = java.type("org.testcontainers.utility.DockerImageName")
FlociContainer = java.type("io.floci.testcontainers.FlociContainer")
FLOCI_IMAGE = DockerImageName.parse("floci/floci:1.5.18")


@pytest.fixture(scope="module")
def floci_container():
    floci = FlociContainer(FLOCI_IMAGE)
    floci.start()  # <2>
    try:
        yield floci
    finally:
        floci.stop()


@pytest.fixture
def my_context(request, floci_container):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={  # <3>
                "aws.access-key-id": str(floci_container.getAccessKey()),
                "aws.secret-key": str(floci_container.getSecretKey()),
                "aws.region": str(floci_container.getRegion()),
                "aws.services.sqs.endpoint-override": str(floci_container.getEndpoint()),
            },
            transactional=False,
        ),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


@pytest.fixture
def demo_consumer(my_context) -> DemoConsumer:
    return my_context[DemoConsumer]


def wait_for_message_count(demo_consumer: DemoConsumer, expected: int, timeout: float = 10.0):
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if demo_consumer.message_count == expected:
            return
        sleep(0.1)
    raise AssertionError(f"Expected {expected} messages but got {demo_consumer.message_count}")


def test_it_works(client, demo_consumer):
    assert demo_consumer.message_count == 0

    response = client.post("/demo", json={})

    assert response.status_code == 204
    wait_for_message_count(demo_consumer, 1)
    assert demo_consumer.message_count == 1
