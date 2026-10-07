import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def client(request):
    context = micronaut_test_fixture(request, MicronautTest(transactional=False))
    yield requests.with_context(context)
    context.stop()


def test_stores_and_finds_nearest_vectors(client):
    for content in (
        "Micronaut is a JVM framework for fast microservices",
        "Micronaut Data generates database queries at compile time",
        "Tomatoes grow well in sunny gardens",
    ):
        response = client.post("/documents", json={"content": content})
        assert response.status_code == 201
        assert response.json()["id"] is not None
        assert response.json()["content"] == content

    response = client.get("/documents/search", params={"q": "JVM framework for microservices"})

    assert response.status_code == 200
    matches = response.json()
    assert len(matches) == 3
    assert matches[0]["content"] == "Micronaut is a JVM framework for fast microservices"
    assert matches[0]["similarity"] > .7
    assert [match["similarity"] for match in matches] == sorted(
        (match["similarity"] for match in matches), reverse=True
    )


@pytest.mark.parametrize("content", [None, "", "   "])
def test_rejects_empty_content(client, content):
    assert client.post("/documents", json={"content": content}).status_code == 400


def test_rejects_empty_query(client):
    assert client.get("/documents/search", params={"q": "   "}).status_code == 400
