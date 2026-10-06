from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class GithubUser:
    login: str
    name: str | None = None
    email: str | None = None
