from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import HttpResponse, MediaType
from micronaut.http.annotation import Body, Consumes, Post, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from ..repositories.room_repository import RoomRepository
from .application_controller import redirect_to

room_repository: Annotated[RoomRepository, Inject]  # <2>


# tag::clazz[]
@ExecuteOn(TaskExecutors.BLOCKING)  # <3>
@Produces(MediaType.TEXT_HTML)  # <4>
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  # <5>
@Post("/rooms")  # <6>
def save(name: Annotated[str, Body("name")]) -> HttpResponse:  # <7>
    return redirect_to("/rooms", room_repository.save(name).id)
# end::clazz[]
