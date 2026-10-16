import json
import logging
import re

import pytest
import requests
from logback import capture_logs, dictConfig
from main import LOGGING
from micronaut.context.annotation import Requires
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.runtime.server import EmbeddedServer
from pyronaut.test import MicronautTest, micronaut_test_fixture

MICRONAUT_RELEASE = re.compile(r"Micronaut (Core |Framework )?v?\d+\.\d+\.\d+( (RC|M)\d)?")


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


def test_http_client_logging_configuration(client):
    assert LOGGING["handlers"]["console"]["level"] == "TRACE"
    dictConfig(LOGGING)
    logger = logging.getLogger("io.micronaut.http.client")
    with capture_logs(logger) as records:
        logger.debug("Fetching GitHub releases")
        assert_releases(client, "/github/releases-lowlevel")
    assert any(record.getMessage() == "Fetching GitHub releases" for record in records)
    assert any(
        record.levelname == "TRACE" and record.getMessage() == "User-Agent: Micronaut HTTP Client"
        for record in records
    )
