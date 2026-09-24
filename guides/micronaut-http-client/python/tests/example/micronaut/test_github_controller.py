import json
import re

import java
import pytest
import requests
from micronaut.context.annotation import Requires
from micronaut.http.annotation import Controller, Get, Produces
from pyronaut.test import MicronautTest, micronaut_test_fixture

MICRONAUT_RELEASE = re.compile(r"Micronaut (Core |Framework )?v?\d+\.\d+\.\d+( (RC|M)\d)?")

EmbeddedServer = java.type("io.micronaut.runtime.server.EmbeddedServer")


@Requires(property="spec.name", value="GithubControllerTest")  # <1>
@Controller
class GithubReleases:

    @Produces("application/vnd.github.v3+json")
    @Get("/repos/micronaut-projects/micronaut-core/releases")
    def core_releases(self) -> str:
        with open("tests-config/releases.json", encoding="utf-8") as releases:  # <3>
            return releases.read()


@pytest.fixture
def github_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.codec.json.additional-types": "application/vnd.github.v3+json",
                "spec.name": "GithubControllerTest",
            },
        ),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def my_context(request, github_context):
    github_server = github_context[EmbeddedServer]
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.github.url": f"http://localhost:{github_server.getPort()}",
            },
        ),
    )  # <2>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def assert_releases(client, path: str):
    response = client.get(path)  # <4>

    releases = json.loads(response.text)  # <5>
    assert response.status_code == 200  # <6>
    assert releases  # <7>
    assert all(MICRONAUT_RELEASE.search(release["name"]) for release in releases)


def test_github_releases_can_be_fetched(client):
    assert_releases(client, "/github/releases")
    assert_releases(client, "/github/releases-lowlevel")
