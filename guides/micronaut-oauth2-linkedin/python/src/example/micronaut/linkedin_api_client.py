from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.core.async_.annotation import SingleResult
from micronaut.http import HttpHeaders
from micronaut.http.annotation import Get, Header
from micronaut.http.client.annotation import Client
from org.reactivestreams import Publisher

from .linkedin_me import LinkedInMe


@Client(id="linkedin")  # <1>
class LinkedInApiClient(ABC):

    @Get("/v2/me")  # <2>
    @SingleResult
    @abstractmethod
    def me(
        self,
        authorization: Annotated[str, Header(HttpHeaders.AUTHORIZATION)],  # <4>
    ) -> Publisher:  # <3>
        ...
