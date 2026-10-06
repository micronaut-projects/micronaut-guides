from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class GithubRelease:
    name: str | None = None
    url: str | None = None
