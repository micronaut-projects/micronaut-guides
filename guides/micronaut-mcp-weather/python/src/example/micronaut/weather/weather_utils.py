import re

from example.micronaut.weather.model.alerts import Alerts
from example.micronaut.weather.model.forecast import Forecast
from example.micronaut.weather.model.grid import Grid
from example.micronaut.weather.model.point_response import PointResponse


GRID_URL_PATTERN = re.compile(r".*/gridpoints/([A-Z]{3})/(\d+),(\d+)/forecast$")


def parse_grid(url: str) -> Grid | None:
    match = GRID_URL_PATTERN.match(url)
    if match is None:
        return None
    return Grid(match.group(1), match.group(2), match.group(3))


def get_forecast_url(points: PointResponse) -> str | None:
    return points.properties.forecast if points.properties else None


def format_forecast(forecast: Forecast) -> str:
    periods = forecast.properties.periods if forecast.properties else []
    if not periods:
        return "No forecast available for requested location"
    return "\n---\n".join(
        f"""Temperature: {period.temperature}\u00b0{period.temperatureUnit}
Wind: {period.windSpeed} {period.windDirection}
Forecast: {period.detailedForecast}
"""
        for period in periods
    )


def format_alerts(alerts: Alerts) -> str:
    features = alerts.features or []
    if not features:
        return "There are no watches, warnings or advisories"
    return "\n---\n".join(
        f"""Event: {properties.event}
Area: {properties.areaDesc}
Severity: {properties.severity}
Description: {properties.description}
Instructions: {properties.instruction}"""
        for properties in (feature.properties for feature in features)
        if properties
    )
