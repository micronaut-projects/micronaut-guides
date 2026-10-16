from typing import Annotated

import pytest
import requests
from micronaut.context.annotation import Requires
from micronaut.http.annotation import Controller, Get, PathVariable, Produces
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.get_alert_input import GetAlertInput
from example.micronaut.point import Point
from example.micronaut.weather.model.alerts import Alerts
from example.micronaut.weather.model.feature import Feature
from example.micronaut.weather.model.forecast import Forecast
from example.micronaut.weather.model.forecast_properties import ForecastProperties
from example.micronaut.weather.model.period import Period
from example.micronaut.weather.model.point_properties import PointProperties
from example.micronaut.weather.model.point_response import PointResponse
from example.micronaut.weather.model.properties import Properties


@Requires(property="spec.name", value="WeatherApiTest")
@Controller
class WeatherApi:
    @Produces("application/json")
    @Get("/alerts/active/area/{state}")
    def alerts(self, state: Annotated[str, PathVariable]) -> Alerts:
        return Alerts(
            features=[
                Feature(
                    properties=Properties(
                        areaDesc="San Francisco",
                        event="High Wind Warning",
                        severity="Severe",
                        description=f"Wind warning for {state}",
                        instruction="Stay indoors",
                    )
                )
            ]
        )

    @Produces("application/geo+json")
    @Get("/points/{latitude},{longitude}")
    def points(
        self,
        latitude: Annotated[str, PathVariable],
        longitude: Annotated[str, PathVariable],
    ) -> PointResponse:
        return PointResponse(
            PointProperties(
                forecast="https://api.weather.gov/gridpoints/MTR/88,126/forecast"
            )
        )

    @Produces("application/json")
    @Get("/gridpoints/{grid_id}/{grid_x},{grid_y}/forecast")
    def forecast(
        self,
        grid_id: Annotated[str, PathVariable],
        grid_x: Annotated[str, PathVariable],
        grid_y: Annotated[str, PathVariable],
    ) -> Forecast:
        return Forecast(
            ForecastProperties(
                periods=[
                    Period(
                        temperature=68,
                        temperatureUnit="F",
                        windSpeed="9 mph",
                        windDirection="NW",
                        detailedForecast=f"Clear near {grid_id} grid {grid_x},{grid_y}",
                    )
                ]
            )
        )


@pytest.fixture
def weather_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"spec.name": "WeatherApiTest"},
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def my_context(request, weather_context):
    weather_client = requests.with_context(weather_context)
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.weather.url": weather_client.base_url,
            },
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def tools(my_context):
    return my_context["example.micronaut.Tools"]


def test_mcp_forecast_tool(tools):
    text = tools.getWeatherForecastByLocation(Point(37.7749, -122.4194))

    assert "Temperature: 68\u00b0F" in text
    assert "Forecast: Clear near MTR grid 88,126" in text


def test_mcp_alerts_tool(tools):
    text = tools.getAlerts(GetAlertInput("CA"))

    assert "Event: High Wind Warning" in text
    assert "Instructions: Stay indoors" in text


def test_weather_api_fixture(weather_context):
    weather_client = requests.with_context(weather_context)

    points_response = weather_client.get("/points/37.7749,-122.4194")
    assert points_response.json()["properties"]["forecast"].endswith(
        "/gridpoints/MTR/88,126/forecast"
    )

    forecast_response = weather_client.get("/gridpoints/MTR/88,126/forecast")
    forecast_json = forecast_response.json()
    assert "periods" in forecast_json["properties"], forecast_json
    assert forecast_json["properties"]["periods"][0]["temperature"] == 68

    alerts_response = weather_client.get("/alerts/active/area/CA")
    alerts_json = alerts_response.json()
    assert "features" in alerts_json, alerts_json
    assert alerts_json["features"][0]["properties"]["event"] == "High Wind Warning"


def test_context(my_context):
    assert my_context.isRunning()
