import pytest

from micronaut.security.token import RolesFinder
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=False,
            properties={
                "micronaut.security.oauth2.enabled": False,
                "micronaut.security.oauth2.clients.oci.enabled": False,
                "micronaut.security.token.roles-name": "groups",
            },
        ),
    )
    yield fixture
    fixture.stop()


def test_find_roles(my_context):
    roles_finder = my_context[RolesFinder]

    assert roles_finder.resolveRoles(
        {
            "groups": [
                {
                    "name": "ROLE_ADMIN",
                    "id": "cab3a10ad56935ca1726464bcaa12a34",
                },
            ],
        }
    ) == ["ROLE_ADMIN"]
    assert roles_finder.resolveRoles({}) == []
    assert roles_finder.resolveRoles({"groups": "foo"}) == []
