from dataclasses import dataclass

from micronaut.context.annotation import ConfigurationProperties, Requires
from micronaut.serde.annotation import Serdeable


@Serdeable
@ConfigurationProperties("github")
@Requires(property="github")
@dataclass
class GithubConfiguration:
    organization: str
    repo: str
    username: str | None = None
    token: str | None = None
