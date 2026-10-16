from micronaut.http import HttpResponse, HttpStatus, MediaType
from micronaut.http.annotation import Consumes, Get, Post, Produces
from micronaut.views import View

from ..model.view_model import ViewModel


@View("new")
@Get("/new")
@Produces(MediaType.TEXT_HTML)
def index() -> dict:
    return ViewModel("A Modal Webpage").to_dict()


@Post("/new")
@Consumes([MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_HTML])
@Produces(MediaType.TEXT_HTML)
def post() -> HttpResponse:
    return HttpResponse.status(HttpStatus.FOUND).header("Location", "/success")
