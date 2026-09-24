from typing import Annotated

from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get, PathVariable
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views import View

from .photo import Photo
from .photos_client import PhotosClient


@Controller("/photos")  # <1>
class PhotosController:
    def __init__(self, photos_client: PhotosClient):  # <2>
        self.photos_client = photos_client

    @View("photos/show.html")  # <5>
    @Get(value="/{id}", produces=MediaType.TEXT_HTML)  # <3> <6>
    @ExecuteOn(TaskExecutors.BLOCKING)  # <4>
    def find_by_id(self, id: Annotated[int, PathVariable]) -> dict[str, Photo]:  # <7>
        return {"photo": self.photos_client.find_by_id(id)}
