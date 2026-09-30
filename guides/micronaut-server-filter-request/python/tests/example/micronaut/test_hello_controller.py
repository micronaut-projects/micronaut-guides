import java
import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

ListAppender = java.type("ch.qos.logback.core.read.ListAppender")
LoggerFactory = java.type("org.slf4j.LoggerFactory")
Thread = java.type("java.lang.Thread")


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def formatted_messages(appender) -> set[str]:
    return {
        appender.list.get(index).getFormattedMessage()
        for index in range(appender.list.size())
    }


def test_hello_filter_logging(client):
    appender = ListAppender()
    logger = LoggerFactory.getLogger("example.micronaut.LoggingHeadersFilter")
    logger.addAppender(appender)
    appender.start()

    try:
        response = client.get(
            "/",
            headers={
                "Authorization": "Bearer x",
                "foo": "bar",
            },
        )

        assert response.status_code == 200
        assert response.json() == {"message": "Hello World"}

        for _ in range(20):
            messages = formatted_messages(appender)
            if "foo: bar" in messages and "Authorization: *MASKED*" in messages:
                break
            Thread.sleep(50)

        messages = formatted_messages(appender)
        assert "foo: bar" in messages
        assert "Authorization: Bearer x" not in messages
        assert "Authorization: *MASKED*" in messages
    finally:
        logger.detachAppender(appender)
