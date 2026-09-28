from decimal import Decimal, ROUND_FLOOR

from jakarta.inject import Singleton

from .scale import Scale
from .temperature_client import TemperatureClient


@Singleton
class MicronautguideCommand:

    def __init__(self, temperature_client: TemperatureClient):  # <1>
        self.temperature_client = temperature_client

    def run(self, temperature: Decimal, scale: str | None = None) -> None:
        temperature_scale = Scale.of(scale) if scale else Scale.CELSIUS
        celsius = (
            fahrenheit_to_celsius(temperature)
            if temperature_scale is Scale.FAHRENHEIT
            else temperature
        )
        self.temperature_client.publish_livingroom_temperature(str(celsius).encode("utf-8"))  # <2>
        print("Topic published")


def fahrenheit_to_celsius(temperature: Decimal) -> Decimal:
    return ((temperature - Decimal("32")) * Decimal(5) / Decimal(9)).quantize(
        Decimal("0.01"),
        rounding=ROUND_FLOOR,
    )
