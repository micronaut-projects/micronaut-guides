from micronaut.context.annotation import Requires
from micronaut.http import MutableHttpRequest
from micronaut.http.annotation import ClientFilter, RequestFilter

from .github_configuration import GithubConfiguration


@ClientFilter("/repos/**")  # <1>
@Requires(property="github.username")  # <2>
@Requires(property="github.token")  # <2>
class GithubFilter:

    def __init__(self, configuration: GithubConfiguration):  # <3>
        self.configuration = configuration

    @RequestFilter  # <4>
    def filter(self, request: MutableHttpRequest) -> None:
        request.basicAuth(self.configuration.username, self.configuration.token)  # <5>
