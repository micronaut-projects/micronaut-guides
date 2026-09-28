from micronaut.http import MediaType
from micronaut.http.annotation import Get
from micronaut.http.annotation import Produces

@Get("/hello")  # <1>
@Produces(MediaType.TEXT_PLAIN) # <2>
def index() -> str:
    return "Hello World"  # <3>
