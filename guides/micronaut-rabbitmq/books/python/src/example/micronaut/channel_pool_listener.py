from com.rabbitmq.client import BuiltinExchangeType, Channel
from jakarta.inject import Singleton
from micronaut.rabbitmq.connect import ChannelPoolInitializer


@Singleton
class ChannelPoolListener(ChannelPoolInitializer):

    def initialize(self, channel: Channel, name: str) -> None:
        channel.exchangeDeclare("micronaut", BuiltinExchangeType.DIRECT, True)  # <1>
        channel.queueDeclare("analytics", True, False, False, None)  # <2>
        channel.queueBind("analytics", "micronaut", "analytics")  # <3>
