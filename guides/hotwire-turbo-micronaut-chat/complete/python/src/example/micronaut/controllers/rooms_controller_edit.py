from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import MediaType
from micronaut.http.annotation import Get, PathVariable, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views import View
from micronaut.views.turbo import TurboFrameView

from ..entities.room import Room
from ..repositories.room_repository import RoomRepository
from .rooms_controller import room_model

room_repository: Annotated[RoomRepository, Inject]  # <2>


# tag::clazz[]
@ExecuteOn(TaskExecutors.BLOCKING)  # <3>
@View("/rooms/edit")  # <4>
@Get("/rooms/{id}/edit")  # <5>
@Produces(MediaType.TEXT_HTML)  # <6>
@TurboFrameView("/rooms/_edit")  # <1>
def edit(id: Annotated[int, PathVariable]) -> dict[str, Room] | None:  # <7>
    return room_model(room_repository.findById(id).orElse(None))
# end::clazz[]
