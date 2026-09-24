import java
import pytest

from micronaut.inject.qualifiers import Qualifiers
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=False,
            properties={
                "stadium.fenway.city": "Boston",
                "stadium.fenway.size": 60000,
                "stadium.wrigley.city": "Chicago",
                "stadium.wrigley.size": 45000,
            },
        ),
    )
    yield fixture
    fixture.stop()


def test_stadium_configuration(my_context):
    StadiumConfiguration = java.type("example.micronaut.StadiumConfiguration")

    # <2>
    fenway_configuration = my_context.getBean(
        StadiumConfiguration,
        Qualifiers.byName("fenway"),
    ).asPolyglotValue()
    wrigley_configuration = my_context.getBean(
        StadiumConfiguration,
        Qualifiers.byName("wrigley"),
    ).asPolyglotValue()

    assert fenway_configuration.name == "fenway"
    assert fenway_configuration.size == 60000
    assert wrigley_configuration.name == "wrigley"
    assert wrigley_configuration.size == 45000
