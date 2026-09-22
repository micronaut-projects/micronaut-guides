from typing import Annotated

from jakarta.inject import Inject, Named
from micronaut.http.annotation import Get

from .stadium_configuration import StadiumConfiguration
from .team_configuration import TeamConfiguration


team_configuration: Annotated[TeamConfiguration, Inject]
stadium_configuration: Annotated[StadiumConfiguration, Inject, Named("pnc")]  # <1>


@Get(value="/my/team", produces="application/json")
def team() -> dict:
    return {
        "name": team_configuration.name,
        "color": team_configuration.color,
        "player_names": team_configuration.player_names,
    }


@Get(value="/my/stadium", produces="application/json")
def stadium() -> dict:
    return {
        "name": stadium_configuration.name,
        "city": stadium_configuration.city,
        "size": stadium_configuration.size,
    }
