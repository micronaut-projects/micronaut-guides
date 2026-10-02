from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.views import View

from ..model.view_model import ViewModel


@View("success")
@Get("/success")
@Produces(MediaType.TEXT_HTML)
def index() -> dict:
    return ViewModel("It Worked!").to_dict()
