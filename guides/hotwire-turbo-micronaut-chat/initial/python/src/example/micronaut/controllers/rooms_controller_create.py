from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.views import View


# tag::clazz[]
@View("/rooms/create")  # <3>
@Get("/rooms/create")  # <4>
@Produces(MediaType.TEXT_HTML)  # <5>
def create() -> dict:
    return {}
# end::clazz[]
