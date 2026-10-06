from micronaut.http import HttpResponse, HttpStatus
from micronaut.http.annotation import Get

@Get
def redirect_to_photos() -> HttpResponse:
    return HttpResponse.status(HttpStatus.SEE_OTHER).header("Location", "/photos/1")
