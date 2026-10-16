from abc import ABC, abstractmethod

from micronaut.mqtt.annotation import Topic
from micronaut.mqtt.annotation.v5 import MqttPublisher


@MqttPublisher  # <1>
class TemperatureClient(ABC):

    @Topic("house/livingroom/temperature")  # <2>
    @abstractmethod
    def publish_livingroom_temperature(self, data: bytes) -> None:
        ...
