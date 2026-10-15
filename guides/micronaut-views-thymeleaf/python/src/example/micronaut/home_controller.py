from micronaut.http import HttpResponse
from micronaut.http.annotation import Get
from java.net import URI

@Get
def redirect_to_photos() -> HttpResponse:
    return HttpResponse.seeOther(URI("/photos/1"))
