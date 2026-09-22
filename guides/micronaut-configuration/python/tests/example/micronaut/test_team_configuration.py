import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.team_admin import TeamAdmin


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=False,
            properties={
                "team.name": "evolution",
                "team.color": "green",
                "team.player-names": ["Nirav Assar", "Lionel Messi"],
            },
        ),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def builder_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            start_application=False,
            transactional=False,
            properties={
                "team.name": "evolution",
                "team.color": "green",
                "team.team-admin.manager": "Jerry Jones",
                "team.team-admin.coach": "Tommy O'Neill",
                "team.team-admin.president": "Mark Scanell",
                "team.player-names": ["Nirav Assar", "Lionel Messi"],
            },
        ),
    )
    yield fixture
    fixture.stop()


# tag::teamConfigSpecNoBuilder[]
def test_team_configuration(my_context):
    team_configuration = my_context["example.micronaut.TeamConfiguration"]

    assert team_configuration.name == "evolution"
    assert team_configuration.color == "green"
    assert team_configuration.player_names == ["Nirav Assar", "Lionel Messi"]
# end::teamConfigSpecNoBuilder[]


def test_builder_pattern_plain_usage():
    team_admin = (
        TeamAdmin.builder()
        .with_manager("Nirav")
        .with_coach("Tommy")
        .with_president("Mark")
        .build()
    )

    assert team_admin.manager == "Nirav"
    assert team_admin.coach == "Tommy"
    assert team_admin.president == "Mark"


# tag::teamConfigSpecBuilder[]
def test_team_configuration_builder(builder_context):
    team_configuration = builder_context["example.micronaut.TeamConfiguration"]
    team_admin = team_configuration.builder.build()  # <2>

    assert team_configuration.name == "evolution"
    assert team_configuration.color == "green"
    assert team_configuration.player_names == ["Nirav Assar", "Lionel Messi"]

    assert team_configuration.builder.manager == "Jerry Jones"
    assert team_configuration.builder.coach == "Tommy O'Neill"
    assert team_configuration.builder.president == "Mark Scanell"

    assert team_admin.manager == "Jerry Jones"  # <3>
    assert team_admin.coach == "Tommy O'Neill"
    assert team_admin.president == "Mark Scanell"
# end::teamConfigSpecBuilder[]
