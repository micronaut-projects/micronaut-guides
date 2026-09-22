from typing import Annotated

from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces, QueryValue
from micronaut.views import View

from ..model.view_model import ViewModel


@View("two")
@Get("/two")
@Produces(MediaType.TEXT_HTML)
def index(action: Annotated[str | None, QueryValue] = None) -> dict:
    return ViewModel("Push or Replace?", action=action).to_dict()
