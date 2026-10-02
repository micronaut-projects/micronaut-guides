from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.core.annotation import Blocking
from micronaut.http.annotation import Get, PathVariable
from micronaut.http.client.annotation import Client

from .photo import Photo


@Client(id="photosapi")  # <1>
class PhotoServiceClient(ABC):

    @Get("/albums/{albumId}/photos")
    @Blocking
    @abstractmethod
    def get_photos(self, albumId: Annotated[int, PathVariable]) -> list[Photo]:
        ...
