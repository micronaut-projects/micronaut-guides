from dataclasses import dataclass, field
from typing import Annotated

from micronaut.context.annotation import ConfigurationBuilder, ConfigurationProperties
from micronaut.serde.annotation import Serdeable

from .team_admin import TeamAdmin, TeamAdminBuilder


# tag::teamConfigClass[]
@Serdeable  # <1>
@ConfigurationProperties("team")
@dataclass(init=False)
class TeamConfiguration:
    name: str | None = None
    color: str | None = None
    player_names: list[str] = field(default_factory=list)
# end::teamConfigClass[]

    builder: Annotated[
        TeamAdminBuilder,
        ConfigurationBuilder(prefixes="with_", configurationPrefix="team-admin"),
    ] = field(init=False)  # <3> <4>

    def __init__(
        self,
        name: str | None = None,
        color: str | None = None,
        player_names: list[str] | None = None,
    ):
        self.name = name
        self.color = color
        self.player_names = player_names or []
        self.builder = TeamAdmin.builder()
