from jakarta.inject import Singleton
from micronaut.context import MessageSource, StaticMessageSource
from micronaut.context.annotation import Factory

from .password_match import MESSAGE


@Factory
class PasswordMatchMessages:
    PASSWORD_MATCH_MESSAGE = "Passwords do not match"

    @Singleton  # <1>
    def message_source(self) -> MessageSource:
        source = StaticMessageSource()
        source.addMessage(MESSAGE, self.PASSWORD_MATCH_MESSAGE)
        return source
