from typing import Annotated

from example.micronaut import GithubRelease
from jakarta.inject import Inject, Singleton
from micronaut.core.type import Argument
from micronaut.http import HttpRequest
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client
from micronaut.http.uri import UriBuilder

from .github_configuration import GithubConfiguration


@Singleton  # <1>
class GithubLowLevelClient:
    http_client: Annotated[HttpClient, Inject, Client(id="github")]  # <2>
    configuration: Annotated[GithubConfiguration, Inject]  # <3>

    async def fetch_releases(self) -> list[GithubRelease]:
        uri = (
            UriBuilder.of("/repos")
            .path(self.configuration.organization)
            .path(self.configuration.repo)
            .path("releases")
            .build()
        )
        request = (
            HttpRequest.GET(uri)  # <4>
            .header("User-Agent", "Micronaut HTTP Client")  # <5>
            .header("Accept", "application/vnd.github.v3+json, application/json")  # <6>
        )
        return await self.http_client.retrieve(request, Argument.listOf(GithubRelease))  # <7>
