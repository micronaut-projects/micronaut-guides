from com.rabbitmq.client import BuiltinExchangeType, Channel
from jakarta.inject import Singleton
from micronaut.rabbitmq.connect import ChannelPoolInitializer


@Singleton
class ChannelPoolListener(ChannelPoolInitializer):

    def initialize(self, channel: Channel, name: str) -> None:
        channel.exchangeDeclare("micronaut", BuiltinExchangeType.DIRECT, True)

        channel.queueDeclare("inventory", True, False, False, None)
        channel.queueBind("inventory", "micronaut", "books.inventory")

        channel.queueDeclare("catalogue", True, False, False, None)
        channel.queueBind("catalogue", "micronaut", "books.catalogue")
