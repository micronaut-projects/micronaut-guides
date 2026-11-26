from abc import ABC, abstractmethod

from micronaut.http.annotation import Get, Header
from micronaut.http.client.annotation import Client

from .github_release import GithubRelease


@Client(id="github")  # <1>
@Header(name="User-Agent", value="Micronaut HTTP Client")  # <2>
@Header(name="Accept", value="application/vnd.github.v3+json, application/json")  # <3>
class GithubApiClient(ABC):

    @Get("/repos/${github.organization}/${github.repo}/releases")  # <4>
    @abstractmethod
    async def fetch_releases(self) -> list[GithubRelease]:  # <5>
        ...
