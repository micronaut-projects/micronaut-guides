from micronaut.http import HttpStatus, MediaType
from micronaut.http.annotation import Get, Produces, Status


@Status(HttpStatus.NOT_FOUND)
@Get("/nonexistent")
@Produces(MediaType.TEXT_HTML)
def index() -> str:
    return "Not Found"
