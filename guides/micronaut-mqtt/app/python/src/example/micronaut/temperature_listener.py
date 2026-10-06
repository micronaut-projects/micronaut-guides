import logging
from decimal import Decimal

from micronaut.mqtt.annotation import MqttSubscriber, Topic


LOG = logging.getLogger(__name__)


@MqttSubscriber  # <1>
class TemperatureListener:

    def __init__(self):
        self._temperature: Decimal | None = None

    @Topic("house/livingroom/temperature")  # <2>
    def receive(self, data: bytes) -> None:
        self._temperature = Decimal(bytes(data).decode("utf-8"))
        LOG.info("temperature: %s", self._temperature)

    @property
    def temperature(self) -> Decimal | None:
        return self._temperature
