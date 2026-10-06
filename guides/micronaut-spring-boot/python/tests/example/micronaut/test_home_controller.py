import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_render_server_side_html_with_thymeleaf_and_micronaut_views(client):
    expected = """<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h1>Welcome to Micronaut for Spring</h1>
</body>
</html>"""

    response = client.get("/")

    assert response.status_code == 200
    assert response.text.strip() == expected
