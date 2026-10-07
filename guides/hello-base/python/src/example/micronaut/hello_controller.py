from micronaut.http import MediaType
from micronaut.http.annotation import Get


@Get(value="/hello", produces=MediaType.TEXT_PLAIN)  # <1> <2>
def index() -> str:
    return "Hello World"  # <3>
