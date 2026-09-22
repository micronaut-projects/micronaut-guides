from abc import ABC, abstractmethod

import java
from micronaut.core.async_.annotation import SingleResult
from micronaut.http.annotation import Get, Header
from micronaut.http.client.annotation import Client

from .github_release import GithubRelease

Publisher = java.type("org.reactivestreams.Publisher")


@Client(id="github")  # <1>
@Header(name="User-Agent", value="Micronaut HTTP Client")  # <2>
@Header(name="Accept", value="application/vnd.github.v3+json, application/json")  # <3>
class GithubApiClient(ABC):

    @Get("/repos/${github.organization}/${github.repo}/releases")  # <4>
    @SingleResult  # <5>
    @abstractmethod
    def fetch_releases(self) -> Publisher[list[GithubRelease]]:  # <6>
        ...
