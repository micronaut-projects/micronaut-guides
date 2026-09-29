from typing import Annotated

from jakarta.inject import Inject
from micronaut.context import LocalizedMessageSource
from micronaut.http import MediaType
from micronaut.http.annotation import Get

message_source: Annotated[LocalizedMessageSource, Inject]  # <1>

@Get # <2>
@Produces(MediaType.TEXT_PLAIN) # <3>
def index() -> str:
    return message_source.getMessage("hello.world").orElse("Hello World")  # <4>
