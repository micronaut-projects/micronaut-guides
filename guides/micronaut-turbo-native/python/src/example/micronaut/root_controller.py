from micronaut.http import HttpRequest, MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.views import View

from .model.view_model import ViewModel


@View("index")
@Get("/")
@Produces(MediaType.TEXT_HTML)
def index(request: HttpRequest) -> dict:
    principal = request.getUserPrincipal().orElse(None)
    return ViewModel("Turbo Native Demo", "index", principal=principal).to_dict()
