from jakarta.inject import Singleton
from micronaut.mcp.annotations import Tool

from example.micronaut.get_alert_input import GetAlertInput
from example.micronaut.point import Point
from example.micronaut.weather.weather_client import WeatherClient


@Singleton  # <1>
class Tools:
    def __init__(self, weather_client: WeatherClient):  # <2>
        self.weather_client = weather_client

    @Tool(
        description="Get weather forecast for a specific latitude/longitude coordinates",
    )  # <3>
    def getWeatherForecastByLocation(self, point: Point) -> str:
        return self.weather_client.formatted_forecast(point.latitude, point.longitude)

    @Tool(description="Get weather alerts for a US state")  # <3>
    def getAlerts(self, input: GetAlertInput) -> str:
        return self.weather_client.formatted_alerts(input.state)
