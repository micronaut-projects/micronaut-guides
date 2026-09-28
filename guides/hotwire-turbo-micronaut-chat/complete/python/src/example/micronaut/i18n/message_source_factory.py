from micronaut.context import MessageSource
from micronaut.context.annotation import Factory
from micronaut.context.i18n import ResourceBundleMessageSource
from jakarta.inject import Singleton


# tag::clazz[]
@Factory  # <1>
class MessageSourceFactory:
    @Singleton  # <2>
    def create_message_source(self) -> MessageSource:
        return ResourceBundleMessageSource("i18n.messages")
# end::clazz[]
