import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.user_role import UserRole
from example.micronaut.user_role_id import UserRoleId

ROLE_USER = "ROLE_USER"
ROLE_ADMIN = "ROLE_ADMIN"
U_SERGIO = "sergio"
U_TIM = "tim"


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False, transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def role_repo(my_context):
    return my_context["example.micronaut.RoleJdbcRepository"]


@pytest.fixture
def user_repo(my_context):
    return my_context["example.micronaut.UserJdbcRepository"]


@pytest.fixture
def user_role_repo(my_context):
    return my_context["example.micronaut.UserRoleJdbcRepository"]


def test_many_to_many_persistence(role_repo, user_repo, user_role_repo):
    role_user = role_repo.save(ROLE_USER)
    role_admin = role_repo.save(ROLE_ADMIN)

    assert user_repo.findByUsername(U_SERGIO).isEmpty()

    sergio = user_repo.save(U_SERGIO)
    assert_user(user_repo.findByUsername(U_SERGIO).orElse(None), U_SERGIO, None)

    user_role_repo.save(UserRole(UserRoleId(sergio, role_user)))
    user_role_repo.save(UserRole(UserRoleId(sergio, role_admin)))
    assert_user(
        user_repo.findByUsername(U_SERGIO).orElse(None),
        U_SERGIO,
        [ROLE_ADMIN, ROLE_USER],
    )

    tim = user_repo.save(U_TIM)
    user_role_repo.save(UserRole(UserRoleId(tim, role_user)))
    assert_user(
        user_repo.findByUsername(U_TIM).orElse(None),
        U_TIM,
        [ROLE_USER],
    )


def assert_user(user, expected_username: str, expected_authorities: list[str] | None):
    assert user is not None
    assert user.id is not None
    assert user.username == expected_username
    assert user.authorities == expected_authorities
