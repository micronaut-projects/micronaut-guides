from datetime import datetime, timezone
import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.generate_git_properties import generate_git_properties, _property_value


COMMIT_MESSAGE = "Initial project\n\nRésumé café 🐍\n\tkey=value: #! C:\\docs\nForm\fFeed\rBack\bSpace\n"
AUTHOR = "Sérgio 🐍"
COMMIT_DATE = "2001-02-03T04:05:06+00:00"


@pytest.fixture(params=[False, True], ids=["clean", "dirty"])
def git_repository(tmp_path, request):
    def git(*args):
        return subprocess.run(
            ["git", "-C", str(tmp_path), *args],
            check=True,
            stdout=subprocess.PIPE,
            env={
                **os.environ,
                "GIT_AUTHOR_DATE": COMMIT_DATE,
                "GIT_COMMITTER_DATE": COMMIT_DATE,
                "GIT_CONFIG_NOSYSTEM": "1",
                "GIT_CONFIG_GLOBAL": os.devnull,
            },
        ).stdout.decode("utf-8").removesuffix("\n")

    git("init", "--initial-branch=main")
    git("config", "user.name", AUTHOR)
    git("config", "user.email", "author@example.com")
    git("commit", "--allow-empty", "--cleanup=verbatim", "-m", COMMIT_MESSAGE)
    if request.param:
        (tmp_path / "uncommitted.txt").write_text("Uncommitted change", encoding="utf-8")
    return generate_git_properties(tmp_path), git("rev-parse", "HEAD"), request.param


@pytest.fixture
def my_context(request, git_repository):
    properties, _, _ = git_repository
    with TemporaryDirectory(prefix="git-info-", dir="config") as resources:
        resource = Path(resources) / "git.properties"
        resource.write_bytes(properties.read_bytes())
        fixture = micronaut_test_fixture(
            request,
            MicronautTest(
                transactional=False,
                properties={
                    "endpoints.info.git.location": f"{Path(resources).name}/git.properties",
                },
            ),  # <1>
        )
        try:
            yield fixture
        finally:
            fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:  # <2>
        yield session


def test_info_endpoint_exposes_git_commit_info(client, git_repository):
    _, commit_id, dirty = git_repository
    response = client.get("/info")  # <3>

    assert response.status_code == 200
    body = response.json()  # <4>
    assert body["git"] == {
        "branch": "main",
        "commit": {
            "id": commit_id,
            "message": {"short": "Initial project", "full": COMMIT_MESSAGE},
            "time": str(int(datetime(2001, 2, 3, 4, 5, 6, tzinfo=timezone.utc).timestamp())),
            "user": {"name": AUTHOR, "email": "author@example.com"},
        },
        "dirty": str(dirty).lower(),
    }
    assert len(body["git"]["commit"]["id"]) == 40


def test_property_values_escape_unicode_controls_and_separators():
    assert _property_value(" é🐍\n\r\t\f\b=:# !\\") == (
        "\\ \\u00e9\\ud83d\\udc0d\\n\\r\\t\\f\\u0008\\=\\:\\#\\ \\!\\\\"
    )
