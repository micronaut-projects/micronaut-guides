from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import HttpResponse, MediaType
from micronaut.http.annotation import Consumes, PathVariable, Post, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from ..repositories.room_repository import RoomRepository

room_repository: Annotated[RoomRepository, Inject]  # <2>


# tag::clazz[]
@ExecuteOn(TaskExecutors.BLOCKING)  # <3>
@Produces(MediaType.TEXT_HTML)  # <4>
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  # <5>
@Post("/rooms/{id}/delete")  # <6>
def delete(id: Annotated[int, PathVariable]) -> HttpResponse:  # <7>
    room_repository.deleteById(id)
    return HttpResponse.seeOther(HttpResponse.uri("/rooms"))
# end::clazz[]
