from micronaut.http import HttpResponse
from micronaut.http.annotation import Get
from micronaut.http.uri import UriBuilder


# tag::clazz[]
@Get("/")  # <2>
def index() -> HttpResponse:  # <1>
    return HttpResponse.seeOther(UriBuilder.of("/rooms").build())
# end::clazz[]
