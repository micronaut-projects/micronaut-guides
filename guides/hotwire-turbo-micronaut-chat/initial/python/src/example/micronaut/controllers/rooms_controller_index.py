from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views import View

from ..repositories.room_repository import RoomRepository
from .rooms_controller import ROOMS

room_repository: Annotated[RoomRepository, Inject]  # <2>


# tag::clazz[]
@ExecuteOn(TaskExecutors.BLOCKING)  # <3>
@View("/rooms/index")  # <4>
@Get("/rooms")  # <5>
@Produces(MediaType.TEXT_HTML)  # <6>
def index() -> dict:
    return {ROOMS: room_repository.findAll()}
# end::clazz[]
