import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture

PATH = "/books"
TITLE = "Netty in Action"


@pytest.fixture
def my_context(request, datasource):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False, properties=datasource),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


@pytest.fixture
def repository(my_context):
    return my_context["example.micronaut.BookRepository"]  # <3>


def test_book_upsert(client, repository):
    assert repository.findByTitle(TITLE) is None  # <4>

    response = client.post(PATH, json={"id": 123, "title": TITLE})
    assert response.status_code == 201  # <5>
    assert repository.findByTitle(TITLE) is not None
    assert repository.findByTitle(TITLE).pages is None

    response = client.put(PATH, json={"id": 123, "title": TITLE, "pages": 265})
    assert response.status_code == 200  # <6>
    assert repository.findByTitle(TITLE).pages == 265  # <7>
