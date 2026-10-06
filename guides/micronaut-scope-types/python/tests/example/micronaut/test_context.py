import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


def test_context_scope_validates_configuration_on_startup(request):
    with pytest.raises(BaseException) as exc:
        micronaut_test_fixture(
            request,
            MicronautTest(
                start_application=False,
                transactional=False,
                properties={"framework.language": "scala"},
            ),
        )

    assert 'language - must match "groovy|java|kotlin|python"' in str(exc.value)
