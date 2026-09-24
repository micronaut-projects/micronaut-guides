from typing import Annotated

import java
from jakarta.inject import Inject
from micronaut.core.async_.annotation import SingleResult
from micronaut.http.annotation import Get

from .github_api_client import GithubApiClient
from .github_low_level_client import GithubLowLevelClient

Publisher = java.type("org.reactivestreams.Publisher")

github_low_level_client: Annotated[GithubLowLevelClient, Inject]  # <1>
github_api_client: Annotated[GithubApiClient, Inject]  # <1>


@Get("/github/releases-lowlevel")  # <2>
@SingleResult  # <3>
def releases_with_low_level_client() -> Publisher:
    return github_low_level_client.fetch_releases()


@Get("/github/releases")  # <4>
@SingleResult  # <3>
def fetch_releases() -> Publisher:  # <5>
    return github_api_client.fetch_releases()
