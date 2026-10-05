from micronaut.context import LocalizedMessageSource
from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get


# tag::clazz[]
@Controller("/")
class HelloWorldController:
    def __init__(self, message_source: LocalizedMessageSource):  # <1>
        self.message_source = message_source

    @Get(produces=MediaType.TEXT_PLAIN)  # <2> <3>
    def index(self) -> str:
        return self.message_source.getMessageOrDefault("hello.world", "Hello World")  # <4>
# end::clazz[]
