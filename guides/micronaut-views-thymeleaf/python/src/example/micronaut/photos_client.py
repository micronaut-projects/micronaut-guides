from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.core.annotation import Blocking
from micronaut.http.annotation import Get, PathVariable
from micronaut.http.client.annotation import Client

from .photo import Photo


@Client(id="photos")  # <1>
class PhotosClient(ABC):

    @Get("/photos/{id}")  # <2>
    @Blocking
    @abstractmethod
    def find_by_id(self, id: Annotated[int, PathVariable]) -> Photo:  # <3>
        ...
