# tag::package[]
from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.core.async_.annotation import SingleResult
from micronaut.http import HttpHeaders
from micronaut.http.annotation import Get, Header, QueryValue
from micronaut.http.client.annotation import Client
from org.reactivestreams import Publisher

from .github_repo import GithubRepo
from .github_user import GithubUser
# end::package[]


# tag::clazz[]
@Header(name="User-Agent", value="https://micronautguides.com")
@Header(name="Accept", value="application/vnd.github.v3+json, application/json")  # <1>
@Client(id="githubv3")  # <2>
class GithubApiClient(ABC):

    @Get("/user")  # <3>
    @SingleResult
    @abstractmethod
    def get_user(
        self,
        authorization: Annotated[str, Header(HttpHeaders.AUTHORIZATION)],  # <5>
    ) -> Publisher:  # <4>
        ...
# end::clazz[]

    # tag::repos[]
    @Get("/user/repos{?sort,direction}")  # <1>
    @abstractmethod
    def repos(
        self,
        sort: Annotated[str | None, QueryValue],  # <2>
        direction: Annotated[str | None, QueryValue],  # <2>
        authorization: Annotated[str, Header(HttpHeaders.AUTHORIZATION)],
    ) -> list[GithubRepo]:
        ...
    # end::repos[]
