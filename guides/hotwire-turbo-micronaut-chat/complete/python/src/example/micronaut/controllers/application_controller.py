from micronaut.http import HttpResponse
from micronaut.http.uri import UriBuilder


# tag::clazz[]
def redirect_to(uri: str, id: int) -> HttpResponse:
    return HttpResponse.seeOther(UriBuilder.of(uri).path(str(id)).build())
# end::clazz[]
