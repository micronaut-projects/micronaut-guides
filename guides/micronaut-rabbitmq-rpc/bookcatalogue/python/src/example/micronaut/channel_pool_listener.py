from com.rabbitmq.client import BuiltinExchangeType, Channel
from jakarta.inject import Singleton
from micronaut.rabbitmq.connect import ChannelPoolInitializer


@Singleton
class ChannelPoolListener(ChannelPoolInitializer):

    def initialize(self, channel: Channel, name: str) -> None:
        channel.exchangeDeclare("micronaut", BuiltinExchangeType.DIRECT, True)  # <1>

        channel.queueDeclare("inventory", True, False, False, None)  # <2>
        channel.queueBind("inventory", "micronaut", "books.inventory")  # <3>

        channel.queueDeclare("catalogue", True, False, False, None)  # <4>
        channel.queueBind("catalogue", "micronaut", "books.catalogue")  # <5>
