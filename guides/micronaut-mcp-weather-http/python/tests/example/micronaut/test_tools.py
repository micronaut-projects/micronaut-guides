from typing import Annotated

import java
import pytest
import requests
from micronaut.context.annotation import Requires
from micronaut.http.annotation import Controller, Get, PathVariable, Produces
from pyronaut.test import MicronautTest, micronaut_test_fixture


CallToolRequest = java.type("io.modelcontextprotocol.spec.McpSchema$CallToolRequest")
HashMap = java.type("java.util.HashMap")
McpSyncClient = java.type("io.modelcontextprotocol.client.McpSyncClient")
TextContent = java.type("io.modelcontextprotocol.spec.McpSchema$TextContent")


@Requires(property="spec.name", value="WeatherApiTest")
@Controller
class WeatherApi:
    @Produces("application/json")
    @Get("/alerts/active/area/{state}")
    def alerts(self, state: Annotated[str, PathVariable]) -> dict:
        return {
            "features": [
                {
                    "properties": {
                        "areaDesc": "San Francisco",
                        "event": "High Wind Warning",
                        "severity": "Severe",
                        "description": f"Wind warning for {state}",
                        "instruction": "Stay indoors",
                    }
                }
            ]
        }

    @Produces("application/geo+json")
    @Get("/points/{latitude},{longitude}")
    def points(
        self,
        latitude: Annotated[str, PathVariable],
        longitude: Annotated[str, PathVariable],
    ) -> dict:
        return {
            "properties": {
                "forecast": "https://api.weather.gov/gridpoints/MTR/88,126/forecast"
            }
        }

    @Produces("application/json")
    @Get("/gridpoints/{grid_id}/{grid_x},{grid_y}/forecast")
    def forecast(
        self,
        grid_id: Annotated[str, PathVariable],
        grid_x: Annotated[str, PathVariable],
        grid_y: Annotated[str, PathVariable],
    ) -> dict:
        return {
            "properties": {
                "periods": [
                    {
                        "temperature": 68,
                        "temperatureUnit": "F",
                        "windSpeed": "9 mph",
                        "windDirection": "NW",
                        "detailedForecast": (
                            f"Clear near {grid_id} grid {grid_x},{grid_y}"
                        ),
                    }
                ]
            }
        }


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
def mcp_client(my_context):
    return my_context[McpSyncClient]


def call_tool(mcp_client, name: str, arguments: dict[str, object]):
    java_arguments = HashMap()
    for key, value in arguments.items():
        java_arguments.put(key, value)
    return mcp_client.callTool(CallToolRequest(name, java_arguments))


def tool_text(result) -> str:
    content = result.content()
    assert content.size() == 1
    text_content = content.get(0)
    assert isinstance(text_content, TextContent)
    return text_content.text()


def test_mcp_forecast_tool(mcp_client):
    result = call_tool(
        mcp_client,
        "getWeatherForecastByLocation",
        {"latitude": 37.7749, "longitude": -122.4194},
    )

    text = tool_text(result)
    assert "Temperature: 68\u00b0F" in text
    assert "Forecast: Clear near MTR grid 88,126" in text


def test_mcp_alerts_tool(mcp_client):
    result = call_tool(mcp_client, "getAlerts", {"state": "CA"})

    text = tool_text(result)
    assert "Event: High Wind Warning" in text
    assert "Instructions: Stay indoors" in text


def test_mcp_list_tools(mcp_client):
    tools = mcp_client.listTools().tools()

    assert tools.size() == 2
    names = {tools.get(index).name() for index in range(tools.size())}
    assert names == {"getWeatherForecastByLocation", "getAlerts"}
