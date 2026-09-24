import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.client.follow-redirects": "false",
                "micronaut.security.reject-not-found": "false",
                "datasources.default.initialization-fail-timeout": "60000",
            },
        ),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


@pytest.fixture
def register_service(my_context):
    return my_context["example.micronaut.RegisterService"]


@pytest.fixture
def user_repository(my_context):
    return my_context["example.micronaut.repositories.UserJdbcRepository"]


@pytest.fixture
def role_repository(my_context):
    return my_context["example.micronaut.repositories.RoleJdbcRepository"]


@pytest.fixture
def user_role_repository(my_context):
    return my_context["example.micronaut.repositories.UserRoleJdbcRepository"]


@pytest.fixture
def repositories(user_role_repository, role_repository, user_repository):
    yield
    user_role_repository.deleteAll()
    role_repository.deleteAll()
    user_repository.deleteAll()


def assert_status(response, expected_status: int):
    if response.status_code != expected_status:
        raise AssertionError(
            f"Expected {expected_status}, got {response.status_code}; "
            f"body={response.text}"
        )


def test_user_can_signup_and_reject_invalid_login(client, repositories):
    response = client.get("/user/signup", headers={"Accept": "text/html"})
    assert_status(response, 200)
    assert "Signup" in response.text

    response = client.post(
        "/user/signup",
        data={
            "username": "admin",
            "password": "admin123",
            "repeatPassword": "admin123",
        },
        headers={"Content-Type": "application/x-www-form-urlencoded"},
    )  # <5>
    assert_status(response, 200)
    assert "Username" in response.text

    response = client.post(
        "/login",
        data={"username": "admin", "password": "wrong"},
        headers={"Content-Type": "application/x-www-form-urlencoded"},
    )  # <6>
    assert_status(response, 200)
    assert "That username or password is incorrect" in response.text


def test_signup_rejects_duplicate_and_mismatched_passwords(client, repositories):
    response = client.post(
        "/user/signup",
        data={
            "username": "admin",
            "password": "admin123",
            "repeatPassword": "admin123",
        },
        headers={"Content-Type": "application/x-www-form-urlencoded"},
    )
    assert_status(response, 200)
    assert "Username" in response.text

    response = client.post(
        "/user/signup",
        data={
            "username": "admin",
            "password": "admin123",
            "repeatPassword": "admin123",
        },
        headers={"Content-Type": "application/x-www-form-urlencoded"},
    )
    assert_status(response, 422)
    assert "Sorry, someone already has that username" in response.text

    response = client.post(
        "/user/signup",
        data={
            "username": "other",
            "password": "admin123",
            "repeatPassword": "wrong",
        },
        headers={"Content-Type": "application/x-www-form-urlencoded"},
    )
    assert_status(response, 422)
    assert "Passwords do not match" in response.text


def test_home_page_links_to_authentication_forms(client):
    response = client.get("/", headers={"Accept": "text/html"})  # <4>
    assert_status(response, 200)
    assert "/user/auth" in response.text
    assert "/user/signup" in response.text


def test_register_service_persists_database_user(
    repositories,
    register_service,
    user_repository,
    role_repository,
    user_role_repository,
):
    register_service.register("admin", "admin123", ["ROLE_USER", "ROLE_ADMIN"])  # <3>

    assert user_repository.count() == 1
    assert role_repository.count() == 2
    assert user_role_repository.count() == 2
    assert user_role_repository.findAllAuthoritiesByUsername("admin") == [
        "ROLE_USER",
        "ROLE_ADMIN",
    ]
