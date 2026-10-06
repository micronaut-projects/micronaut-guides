from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get

from .github_api_client import GithubApiClient
from .github_low_level_client import GithubLowLevelClient
from .github_release import GithubRelease

github_low_level_client: Annotated[GithubLowLevelClient, Inject]  # <1>
github_api_client: Annotated[GithubApiClient, Inject]  # <1>


@Get("/github/releases-lowlevel")  # <2>
async def releases_with_low_level_client() -> list[GithubRelease]:
    return await github_low_level_client.fetch_releases()  # <3>


@Get("/github/releases")  # <4>
async def fetch_releases() -> list[GithubRelease]:
    return await github_api_client.fetch_releases()  # <5>
