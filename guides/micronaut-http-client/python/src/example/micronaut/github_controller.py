from micronaut.http.annotation import Controller, Get

from .github_api_client import GithubApiClient
from .github_release import GithubRelease


@Controller("/github")  # <1>
class GithubController:

    def __init__(self, github_api_client: GithubApiClient):  # <2>
        self.github_api_client = github_api_client

    @Get("/releases")  # <3>
    async def fetch_releases(self) -> list[GithubRelease]:  # <4>
        return await self.github_api_client.fetch_releases()
