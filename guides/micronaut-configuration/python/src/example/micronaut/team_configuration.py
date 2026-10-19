from dataclasses import dataclass, field
from typing import Annotated

from micronaut.context.annotation import ConfigurationBuilder, ConfigurationProperties

from example.micronaut.team_admin import TeamAdmin, TeamAdminBuilder


# tag::teamConfigClassNoBuilder[]
@ConfigurationProperties("team")
@dataclass(init=False)
class TeamConfiguration:
    name: str | None = None
    color: str | None = None
    player_names: list[str] = field(default_factory=list)
# end::teamConfigClassNoBuilder[]

    builder: Annotated[
        TeamAdminBuilder,
        ConfigurationBuilder(prefixes="with_", configurationPrefix="team-admin"),  # <1>
    ] = field(init=False)

    def __init__(
        self,
        name: str | None = None,
        color: str | None = None,
        player_names: list[str] | None = None,
    ):
        self.name = name
        self.color = color
        self.player_names = player_names if player_names is not None else []
        self.builder = TeamAdmin.builder()  # <2>
