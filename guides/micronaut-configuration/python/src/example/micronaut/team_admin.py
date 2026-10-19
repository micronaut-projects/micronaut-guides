from dataclasses import dataclass


@dataclass
class TeamAdmin:  # <1>
    manager: str | None = None
    coach: str | None = None
    president: str | None = None

    @staticmethod
    def builder() -> "TeamAdminBuilder":
        return TeamAdminBuilder()


@dataclass
class TeamAdminBuilder:  # <2>
    manager: str | None = None
    coach: str | None = None
    president: str | None = None

    def with_manager(self, manager: str) -> "TeamAdminBuilder":  # <3>
        self.manager = manager
        return self

    def with_coach(self, coach: str) -> "TeamAdminBuilder":
        self.coach = coach
        return self

    def with_president(self, president: str) -> "TeamAdminBuilder":
        self.president = president
        return self

    def build(self) -> TeamAdmin:  # <4>
        return TeamAdmin(
            manager=self.manager,
            coach=self.coach,
            president=self.president,
        )
