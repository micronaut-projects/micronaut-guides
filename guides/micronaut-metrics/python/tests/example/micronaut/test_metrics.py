import java
import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture

LoggerFactory = java.type("org.slf4j.LoggerFactory")
LoggingSystem = java.type("io.micronaut.logging.LoggingSystem")
LogLevel = java.type("io.micronaut.logging.LogLevel")
MeterRegistry = java.type("io.micrometer.core.instrument.MeterRegistry")
Tags = java.type("io.micrometer.core.instrument.Tags")
TimeUnit = java.type("java.util.concurrent.TimeUnit")


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <4>


@pytest.fixture
def meter_registry(my_context):
    return my_context[MeterRegistry]  # <2>


@pytest.fixture
def logging_system(my_context):
    return my_context[LoggingSystem]  # <3>


def test_expected_meters(meter_registry):
    names = {meter.getId().getName() for meter in meter_registry.getMeters()}

    assert "jvm.memory.max" in names
    assert "process.uptime" in names
    assert "system.cpu.usage" in names
    assert "process.files.open" in names
    assert "logback.events" in names
    assert "hikaricp.connections.max" in names

    assert "http.client.requests" not in names
    assert "http.server.requests" not in names


def test_http(client, meter_registry):
    timer = meter_registry.timer(
        "http.server.requests",
        Tags.of(
            "exception", "none",
            "method", "GET",
            "status", "200",
            "uri", "/books",
        ),
    )
    assert timer.count() == 0

    book_index_timer = meter_registry.timer(
        "books.index",
        Tags.of("exception", "none"),
    )
    assert book_index_timer.count() == 0

    response = client.get("/books")
    assert response.status_code == 200

    assert timer.count() == 1
    assert book_index_timer.count() == 1
    assert book_index_timer.totalTime(TimeUnit.MILLISECONDS) > 0.0
    assert book_index_timer.max(TimeUnit.MILLISECONDS) > 0.0

    book_find_counter = meter_registry.counter(
        "books.find",
        Tags.of(
            "result", "success",
            "exception", "none",
        ),
    )
    assert book_find_counter.count() == 0

    response = client.get("/books/1491950358")
    assert response.status_code == 200

    assert book_find_counter.count() == 1


def test_logback(meter_registry, logging_system):
    counter = meter_registry.counter("logback.events", Tags.of("level", "info"))
    initial = counter.count()

    logger = LoggerFactory.getLogger("testing.testing")
    logging_system.setLogLevel("testing.testing", LogLevel.ALL)

    logger.trace("trace")
    logger.debug("debug")
    logger.info("info")
    logger.warn("warn")
    logger.error("error")

    assert counter.count() == pytest.approx(initial + 1)


def test_metrics_endpoint(client):
    response = client.get("/metrics")
    assert response.status_code == 200

    payload = response.json()
    assert "names" in payload
    names = payload["names"]

    assert "jvm.memory.max" in names
    assert "process.uptime" in names
    assert "system.cpu.usage" in names
    assert "process.files.open" in names
    assert "logback.events" in names
    assert "hikaricp.connections.max" in names


def test_one_metric_endpoint(client):
    response = client.get("/metrics/jvm.memory.used")
    assert response.status_code == 200

    payload = response.json()
    assert payload["name"] == "jvm.memory.used"
    assert len(payload["measurements"]) == 1
    assert float(payload["measurements"][0]["value"]) > 0.0
