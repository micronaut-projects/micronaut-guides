import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def make_request(client, book_id: str) -> dict:
    query = f"""
    {{
      bookById(id: "{book_id}") {{
        name
        pageCount
        author {{
          firstName
          lastName
        }}
      }}
    }}
    """

    response = client.post("/graphql", json={"query": query})
    assert response.status_code == 200
    return response.json()


def test_graph_ql_controller(client):
    body = make_request(client, "book-1")

    book_info = body["data"]
    assert "bookById" in book_info

    book_by_id = book_info["bookById"]

    assert book_by_id["name"] == "Harry Potter and the Philosopher's Stone"
    assert book_by_id["pageCount"] == 223

    author = book_by_id["author"]
    assert author["firstName"] == "Joanne"
    assert author["lastName"] == "Rowling"


def test_graph_ql_controller_empty_response(client):
    body = make_request(client, "missing-id")

    book_info = body["data"]
    assert "bookById" in book_info
    assert book_info["bookById"] is None
