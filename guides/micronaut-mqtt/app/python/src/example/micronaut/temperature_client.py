from abc import ABC, abstractmethod

from micronaut.context.annotation import Requires
from micronaut.mqtt.annotation import Topic
from micronaut.mqtt.annotation.v5 import MqttPublisher


@Requires(property="spec.name", value="SubscriptionTest")  # <1>
@MqttPublisher  # <2>
class TemperatureClient(ABC):

    @Topic("house/livingroom/temperature")  # <3>
    @abstractmethod
    def publish_livingroom_temperature(self, data: bytes) -> None:
        ...
