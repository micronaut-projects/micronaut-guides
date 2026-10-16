from decimal import Decimal

from micronaut.context.annotation import Requires
from micronaut.mqtt.annotation import MqttSubscriber, Topic


@Requires(property="spec.name", value="MicronautguideCommandTest")
@MqttSubscriber  # <1>
class TemperatureListener:

    def __init__(self):
        self.temperature: Decimal | None = None

    @Topic("house/livingroom/temperature")  # <2>
    def receive(self, data: bytes) -> None:
        self.temperature = Decimal(bytes(data).decode("utf-8"))
