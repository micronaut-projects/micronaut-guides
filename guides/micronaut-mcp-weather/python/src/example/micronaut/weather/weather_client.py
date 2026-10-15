from typing import Annotated

import java
from jakarta.inject import Inject, Singleton
from micronaut.core.type import Argument
from micronaut.http import HttpHeaders, HttpRequest
from micronaut.http.client import HttpClient
from micronaut.http.client.annotation import Client

from example.micronaut.weather import weather_utils


AlertsClass = java.type("example.micronaut.weather.model.Alerts")
ForecastClass = java.type("example.micronaut.weather.model.Forecast")
PointResponseClass = java.type("example.micronaut.weather.model.PointResponse")


@Singleton
class WeatherClient:
    http_client: Annotated[HttpClient, Inject, Client(id="weather")]

    def get_alerts(self, state: str):
        request = self._request(f"/alerts/active/area/{state}")
        return self.http_client.toBlocking().retrieve(request, Argument.of(AlertsClass))

    def get_points(self, latitude: float, longitude: float):
        request = self._request(f"/points/{latitude},{longitude}")
        return self.http_client.toBlocking().retrieve(request, Argument.of(PointResponseClass))

    def get_forecast(self, grid_id: str, grid_x: str, grid_y: str):
        request = self._request(f"/gridpoints/{grid_id}/{grid_x},{grid_y}/forecast")
        return self.http_client.toBlocking().retrieve(request, Argument.of(ForecastClass))

    def formatted_alerts(self, state: str) -> str:
        return weather_utils.format_alerts(self.get_alerts(state))

    def formatted_forecast(self, latitude: float, longitude: float) -> str:
        forecast = self.forecast_for_location(latitude, longitude)
        if forecast is None:
            return "No forecast available for requested location"
        return weather_utils.format_forecast(forecast)

    def forecast_for_location(self, latitude: float, longitude: float):
        forecast_url = weather_utils.get_forecast_url(self.get_points(latitude, longitude))
        if forecast_url is None:
            return None
        grid = weather_utils.parse_grid(forecast_url)
        if grid is None:
            return None
        return self.get_forecast(grid.id, grid.x, grid.y)

    @staticmethod
    def _request(uri: str) -> HttpRequest:
        return HttpRequest.GET(uri).header(HttpHeaders.USER_AGENT, "MCP Server")
