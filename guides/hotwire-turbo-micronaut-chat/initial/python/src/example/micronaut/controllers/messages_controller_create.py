from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import MediaType
from micronaut.http.annotation import Get, PathVariable, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views import View

from ..entities.room import Room
from ..repositories.room_repository import RoomRepository
from .rooms_controller import ROOM

room_repository: Annotated[RoomRepository, Inject]  # <3>


# tag::clazz[]
@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@View("/messages/create")  # <4>
@Produces(MediaType.TEXT_HTML)  # <5>
@Get("/rooms/{id}/messages/create")  # <6>
def create(id: Annotated[int, PathVariable]) -> dict[str, Room] | None:  # <7>
    room = room_repository.findById(id).orElse(None)
    return None if room is None else {ROOM: room}
# end::clazz[]
