from micronaut.context import LocalizedMessageSource
from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get


# tag::clazz[]
@Controller("/")  # <1>
class HelloWorldController:
    def __init__(self, message_source: LocalizedMessageSource):  # <2>
        self.message_source = message_source

    @Get(produces=MediaType.TEXT_PLAIN)  # <3> <4>
    def index(self) -> str:  # <5>
        return self.message_source.get_message_or_default("hello.world", "Hello World")  # <6>
# end::clazz[]
