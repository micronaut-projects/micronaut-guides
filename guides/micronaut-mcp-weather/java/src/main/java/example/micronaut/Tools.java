package example.micronaut;

import example.micronaut.weather.WeatherClient;
import io.micronaut.mcp.annotations.Tool;
import jakarta.inject.Singleton;

@Singleton // <1>
class Tools {

    private final WeatherClient weatherClient;

    Tools(WeatherClient weatherClient) { // <2>
        this.weatherClient = weatherClient;
    }

    @Tool(description = "Get weather forecast for a specific latitude/longitude coordinates") // 3>
    String getWeatherForecastByLocation(double latitude, double longitude) {
        return weatherClient.formattedForecast(latitude, longitude);
    }

    @Tool(description = "Get weather alerts for a US state") // 3>
    String getAlerts(GetAlertInput input) {
        return weatherClient.formattedAlerts(input.state());
    }
}
