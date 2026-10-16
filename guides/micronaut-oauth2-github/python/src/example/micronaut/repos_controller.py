from micronaut.http import HttpHeaderValues
from micronaut.http.annotation import Controller, Get
from micronaut.security.annotation import Secured
from micronaut.security.authentication import Authentication
from micronaut.security.oauth2.endpoint.token.response import OauthAuthenticationMapper
from micronaut.security.rules import SecurityRule
from micronaut.views import View

from .github_api_client import GithubApiClient


@Controller("/repos")  # <1>
class ReposController:
    CREATED = "created"
    DESC = "desc"
    REPOS = "repos"

    def __init__(self, github_api_client: GithubApiClient):
        self.github_api_client = github_api_client

    @Secured(SecurityRule.IS_AUTHENTICATED)  # <2>
    @View("repos")  # <3>
    @Get  # <4>
    def index(self, authentication: Authentication) -> dict:
        repos = self.github_api_client.repos(
            self.CREATED,
            self.DESC,
            self.authorization_value(authentication),
        )  # <5>
        return {self.REPOS: repos}

    def authorization_value(self, authentication: Authentication) -> str | None:
        claim = authentication.getAttributes().get(OauthAuthenticationMapper.ACCESS_TOKEN_KEY)  # <6>
        if isinstance(claim, str):
            return f"{HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER} {claim}"
        return None
