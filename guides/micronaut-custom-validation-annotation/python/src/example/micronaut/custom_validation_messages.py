from jakarta.inject import Singleton
from micronaut.context import MessageSource, StaticMessageSource
from micronaut.context.annotation import Factory

from .e164 import MESSAGE


@Factory
class CustomValidationMessages:
    E164_MESSAGE = "must be a phone in E.164 format"

    @Singleton
    def message_source(self) -> MessageSource:
        source = StaticMessageSource()
        source.addMessage(MESSAGE, self.E164_MESSAGE)
        return source
