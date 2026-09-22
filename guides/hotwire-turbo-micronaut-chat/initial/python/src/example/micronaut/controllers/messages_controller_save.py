from typing import Annotated

from jakarta.inject import Inject
from micronaut.http import HttpResponse, MediaType
from micronaut.http.annotation import Body, Consumes, PathVariable, Post, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from ..models.message_form import MessageForm
from ..services.message_service import MessageService
from .application_controller import redirect_to

message_service: Annotated[MessageService, Inject]  # <3>


# tag::clazz[]
@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Produces(MediaType.TEXT_HTML)  # <4>
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)  # <5>
@Post("/rooms/{id}/messages")  # <6>
def save(
    id: Annotated[int, PathVariable],  # <7>
    content: Annotated[str, Body("content")],  # <8>
) -> HttpResponse:
    room_message = message_service.save(MessageForm(id, content))
    return HttpResponse.notFound() if room_message is None else redirect_to("/rooms", id)
# end::clazz[]
