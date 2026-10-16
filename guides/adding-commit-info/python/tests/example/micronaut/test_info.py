from pathlib import Path
import subprocess

import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.generate_git_properties import generate_git_properties


def _git(*args: str):
    return subprocess.run(["git", *args], check=True, stdout=subprocess.PIPE, text=True)


@pytest.fixture
def my_context(request):
    _prepare_git_properties()
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_info_endpoint_exposes_git_commit_info(client):
    response = client.get("/info")  # <3>

    assert response.status_code == 200
    body = response.json()  # <4>
    assert "git" in body
    assert body["git"]["branch"] == "main"
    assert body["git"]["commit"]["message"]["short"] == "Initial project"
    assert len(body["git"]["commit"]["id"]) == 40


def _prepare_git_properties():
    if not Path(".git").exists():
        _git("init", "-b", "main")
        _git("config", "user.name", "sdelamo")
        _git("config", "user.email", "sergio.delamo@softamo.com")
        _git("add", ".")
        _git("commit", "-m", "Initial project")
    generate_git_properties()
