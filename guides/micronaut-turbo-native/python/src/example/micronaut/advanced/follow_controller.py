from micronaut.http import HttpResponse, MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.http.uri import UriBuilder


@Get("/follow")
@Produces(MediaType.TEXT_HTML)
def index() -> HttpResponse:
    return HttpResponse.temporaryRedirect(UriBuilder.of("/redirected").build())
