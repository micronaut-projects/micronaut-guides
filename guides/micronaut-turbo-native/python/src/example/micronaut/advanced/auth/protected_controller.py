from java.security import Principal
from micronaut.http import HttpResponse, HttpStatus, MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.views import ModelAndView

from ...model.view_model import ViewModel


@Get("/protected")
@Produces(MediaType.TEXT_HTML)
def index(principal: Principal | None = None) -> HttpResponse:
    if principal is None:
        return HttpResponse.status(HttpStatus.UNAUTHORIZED).body("Unauthorized")
    return HttpResponse.ok(
        ModelAndView("protected", ViewModel("Protected Webpage").to_dict())
    )
