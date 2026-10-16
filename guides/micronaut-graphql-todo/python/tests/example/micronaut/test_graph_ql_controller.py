import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


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
    return requests.with_context(my_context)  # <2>


def fetch(client, query: str) -> dict:
    response = client.post("/graphql", json={"query": query})
    assert response.status_code == 200
    body = response.json()
    assert "errors" not in body
    return body


def get_todos(client) -> list[dict]:
    body = fetch(
        client,
        """
        query {
          toDos {
            title
            completed
            author {
              id
              username
            }
          }
        }
        """,
    )
    return body["data"]["toDos"]


def create_todo(client, title: str, author: str) -> int:
    body = fetch(
        client,
        f"""
        mutation {{
          createToDo(title: "{title}", author: "{author}") {{
            id
          }}
        }}
        """,
    )
    return int(body["data"]["createToDo"]["id"])


def mark_as_completed(client, todo_id: int) -> bool:
    body = fetch(
        client,
        f"""
        mutation {{
          completeToDo(id: "{todo_id}")
        }}
        """,
    )
    return body["data"]["completeToDo"]


def test_graph_ql_controller(client):
    assert get_todos(client) == []

    todo_id = create_todo(client, "Test GraphQL", "Tim Yates")
    assert todo_id == 1

    todos = get_todos(client)
    assert len(todos) == 1

    todo = todos[0]
    assert todo["title"] == "Test GraphQL"
    assert todo["completed"] is False
    assert todo["author"]["username"] == "Tim Yates"

    assert mark_as_completed(client, todo_id) is True

    todos = get_todos(client)
    assert len(todos) == 1
    todo = todos[0]
    assert todo["title"] == "Test GraphQL"
    assert todo["completed"] is True
    assert todo["author"]["username"] == "Tim Yates"
