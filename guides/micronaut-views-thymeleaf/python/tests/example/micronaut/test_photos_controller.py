from typing import Annotated

import pytest
import requests
import java
from micronaut.context.annotation import Requires
from micronaut.http.annotation import Controller, Get, PathVariable
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.photo import Photo

EmbeddedServer = java.type("io.micronaut.runtime.server.EmbeddedServer")


@Requires(property="spec.name", value="PhotosControllerTest")
@Controller
class PhotosApi:

    @Get("/photos/{id}")
    def find_by_id(self, id: Annotated[int, PathVariable]) -> Photo:
        return Photo(
            albumId=1,
            title="accusamus beatae ad facilis cum similique qui sunt",
            url="https://via.placeholder.com/600/92c952",
            thumbnailUrl="https://via.placeholder.com/150/92c952",
        )


@pytest.fixture
def photos_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "spec.name": "PhotosControllerTest",
            },
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def my_context(request, photos_context):
    photos_server = photos_context[EmbeddedServer]
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.photos.url": f"http://localhost:{photos_server.getPort()}",
            },
        ),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_photo(client):
    response = client.get("/photos/1", headers={"Accept": "text/html"})

    assert response.status_code == 200
    html = response.text
    assert "<!DOCTYPE html>" in html
    expected_url = "https://via.placeholder.com/600/92c952"
    expected_title = "accusamus beatae ad facilis cum similique qui sunt"
    expected_thumbnail_url = "https://via.placeholder.com/150/92c952"
    assert f"<h1>{expected_title}</h1>" in html
    assert (
        f'<a href="{expected_url}"><img src="{expected_thumbnail_url}" alt="{expected_title}"/></a>'
        in html
    )
