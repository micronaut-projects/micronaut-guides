from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.views import View

from ...model.view_model import ViewModel


@View("reference")
@Get("/reference")
@Produces(MediaType.TEXT_HTML)
def index() -> dict:
    return ViewModel("Reference", "index").to_dict()


@View("turbo-drive")
@Get("/reference/turbo-drive")
@Produces(MediaType.TEXT_HTML)
def turbo_drive() -> dict:
    return ViewModel("Turbo Drive").to_dict()


@View("turbo-frames")
@Get("/reference/turbo-frames")
@Produces(MediaType.TEXT_HTML)
def turbo_frames() -> dict:
    return ViewModel("Turbo Frames").to_dict()


@View("turbo-streams")
@Get("/reference/turbo-streams")
@Produces(MediaType.TEXT_HTML)
def turbo_streams() -> dict:
    return ViewModel("Turbo Streams").to_dict()


@View("turbo-native")
@Get("/reference/turbo-native")
@Produces(MediaType.TEXT_HTML)
def turbo_native() -> dict:
    return ViewModel("Turbo Native").to_dict()
