from typing import Annotated

from micronaut.http.annotation import Controller, Get, PathVariable
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .album import Album
from .photo_service_client import PhotoServiceClient


@Controller("/api")  # <1>
class AlbumController:
    def __init__(self, photo_service_client: PhotoServiceClient):  # <2>
        self.photo_service_client = photo_service_client

    @ExecuteOn(TaskExecutors.BLOCKING)  # <3>
    @Get("/albums/{albumId}")  # <4>
    def get_album_by_id(self, albumId: Annotated[int, PathVariable]) -> Album:  # <5>
        return Album(albumId, self.photo_service_client.get_photos(albumId) or [])
