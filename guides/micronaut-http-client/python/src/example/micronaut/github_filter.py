from typing import Annotated

from jakarta.inject import Inject
from micronaut.context.annotation import Requires
from micronaut.http import MutableHttpRequest
from micronaut.http.annotation import ClientFilter, RequestFilter

from .github_configuration import GithubConfiguration


@ClientFilter("/repos/**")  # <1>
@Requires(property="github.username")  # <2>
@Requires(property="github.token")  # <2>
class GithubFilter:
    configuration: Annotated[GithubConfiguration, Inject]  # <3>

    @RequestFilter  # <4>
    def filter(self, request: MutableHttpRequest) -> None:
        request.basicAuth(self.configuration.username, self.configuration.token)  # <5>
