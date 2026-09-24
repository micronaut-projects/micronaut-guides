from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import MediaType
from micronaut.http.annotation import Get, PathVariable, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views import View

from ..entities.room import Room
from ..repositories.room_repository import RoomRepository
from .rooms_controller import room_model, room_with_messages

room_repository: Annotated[RoomRepository, Inject]  # <2>


# tag::clazz[]
@ExecuteOn(TaskExecutors.BLOCKING)  # <3>
@View("/rooms/show")  # <4>
@Get("/rooms/{id}")  # <5>
@Produces(MediaType.TEXT_HTML)  # <6>
def show(id: Annotated[int, PathVariable]) -> dict[str, Room] | None:  # <7>
    return room_model(room_with_messages(id, room_repository))
# end::clazz[]
