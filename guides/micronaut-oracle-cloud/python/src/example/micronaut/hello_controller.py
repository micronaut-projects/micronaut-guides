from micronaut.http import MediaType
from micronaut.http.annotation import Get


@Get(value="/", produces=MediaType.TEXT_PLAIN)
def index() -> str:
    return "the Micronaut framework on Oracle Cloud"
