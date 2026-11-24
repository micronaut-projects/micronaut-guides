from typing import Annotated

from jakarta.inject import Named
from micronaut.http.annotation import Controller, Get

from example.micronaut.stadium_configuration import StadiumConfiguration
from example.micronaut.team_configuration import TeamConfiguration


@Controller("/my")
class MyController:
    def __init__(
        self,
        team_configuration: TeamConfiguration,
        stadium_configuration: Annotated[StadiumConfiguration, Named("pnc")],  # <1>
    ):
        self.team_configuration = team_configuration
        self.stadium_configuration = stadium_configuration

    @Get("/team")
    def team(self) -> dict:
        return {
            "name": self.team_configuration.name,
            "color": self.team_configuration.color,
            "player_names": self.team_configuration.player_names,
        }

    @Get("/stadium")
    def stadium(self) -> dict:
        return {
            "name": self.stadium_configuration.name,
            "city": self.stadium_configuration.city,
            "size": self.stadium_configuration.size,
        }
