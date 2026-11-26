from dataclasses import dataclass

from micronaut.context.annotation import ConfigurationProperties, Requires


@ConfigurationProperties("github")
@Requires(property="github")
@dataclass
class GithubConfiguration:
    organization: str
    repo: str
    username: str | None = None
    token: str | None = None
