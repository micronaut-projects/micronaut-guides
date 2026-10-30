import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


def test_context_scope_validates_configuration_on_startup(request):
    with pytest.raises(AssertionError) as error:
        micronaut_test_fixture(
            request,
            MicronautTest(
                start_application=False,
                transactional=False,
                properties={"framework.language": "scala"},
            ),
        )

    message = str(error.value)
    assert "BeanInstantiationException" in message
    assert 'language - must match "groovy|java|kotlin|python"' in message
