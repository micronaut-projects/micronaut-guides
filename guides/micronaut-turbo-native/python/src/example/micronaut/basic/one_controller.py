from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.views import View

from ..model.view_model import ViewModel


@View("one")
@Get("/one")
@Produces(MediaType.TEXT_HTML)
def index() -> dict:
    return ViewModel("How’d You Get Here?").to_dict()
