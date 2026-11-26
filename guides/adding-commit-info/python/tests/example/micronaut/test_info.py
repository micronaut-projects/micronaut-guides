from datetime import datetime, timezone
import os
from pathlib import Path
import subprocess
from tempfile import TemporaryDirectory

import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.generate_git_properties import generate_git_properties

COMMIT_MESSAGE = "Initial project\n\nRésumé café 🐍\n\tkey=value: #! C:\\docs\nForm\fFeed\bSpace"
AUTHOR = "Sérgio 🐍"
COMMIT_DATE = datetime(2001, 2, 3, 4, 5, 6, tzinfo=timezone.utc)
GIT_ENV = {  # a fixed date and no user configuration, so every run creates the same commit
    **os.environ,
    "GIT_AUTHOR_DATE": COMMIT_DATE.isoformat(),
    "GIT_COMMITTER_DATE": COMMIT_DATE.isoformat(),
    "GIT_CONFIG_NOSYSTEM": "1",
    "GIT_CONFIG_GLOBAL": os.devnull,
}


@pytest.fixture(params=[False, True], ids=["clean", "dirty"])
def git_repository(tmp_path, request):
    def git(*args):
        return subprocess.check_output(["git", *args], cwd=tmp_path, env=GIT_ENV, text=True).strip()

    readme = tmp_path / "README.md"
    readme.write_text("Initial project\n")
    git("init", "--initial-branch=main")
    git("config", "user.name", AUTHOR)
    git("config", "user.email", "author@example.com")
    git("add", "README.md")
    git("commit", "--cleanup=verbatim", "-m", COMMIT_MESSAGE)
    if request.param:
        readme.write_text("Uncommitted change\n")
    return tmp_path, git("rev-parse", "HEAD"), request.param


@pytest.fixture
def my_context(request, git_repository):
    project_dir, _, _ = git_repository
    properties = generate_git_properties(project_dir)
    # The endpoint loads git.properties from the classpath, so copy it to a temporary folder under config/.
    with TemporaryDirectory(prefix="git-info-", dir="config") as folder:
        Path(folder, "git.properties").write_bytes(properties.read_bytes())
        fixture = micronaut_test_fixture(
            request,
            MicronautTest(properties={"endpoints.info.git.location": f"{Path(folder).name}/git.properties"}),
        )  # <1>
        yield fixture
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
    assert body["git"]["branch"] == "main"
    assert body["git"]["dirty"] == str(dirty).lower()
    commit = body["git"]["commit"]
    assert commit["id"] == commit_id
    assert commit["time"] == str(int(COMMIT_DATE.timestamp()))
    assert commit["message"] == {"short": "Initial project", "full": COMMIT_MESSAGE}
    assert commit["user"] == {"name": AUTHOR, "email": "author@example.com"}
