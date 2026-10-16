from typing import Annotated

from jakarta.inject import Inject
from java.util import Collections
from micronaut.http import HttpRequest, HttpResponse, MediaType
from micronaut.http.annotation import Body, Consumes, PathVariable, Post, Produces
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.views.turbo import TurboStream
from micronaut.views.turbo.http import TurboMediaType

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
    request: HttpRequest,
) -> HttpResponse:
    room_message = message_service.save(MessageForm(id, content))
    if room_message is None:
        return HttpResponse.notFound()
    if TurboMediaType.acceptsTurboStream(request):  # <1>
        return HttpResponse.ok(
            TurboStream.builder()  # <2>
            .template("/messages/_message.html", Collections.singletonMap("message", room_message))
            .targetDomId("messages")
            .append()
        )
    return redirect_to("/rooms", id)
# end::clazz[]
