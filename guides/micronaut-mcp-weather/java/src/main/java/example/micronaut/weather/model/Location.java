package example.micronaut.weather.model;

import io.micronaut.core.annotation.Introspected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@Introspected
public record Location(double latitude, double longitude) {
    private static final Logger LOG = LoggerFactory.getLogger(Location.class);

    public static Optional<Location> of(Map<String, Object> arguments) {
        Object latitudeObj = arguments.get("latitude");
        Object longitudeObj = arguments.get("longitude");
        if (latitudeObj == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Missing 'latitude' in arguments");
            }
            return Optional.empty();
        }
        if (longitudeObj == null) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Missing 'longitude' in arguments");
            }
            return Optional.empty();
        }
        Double latitude = null;
        try {
            latitude = Double.valueOf(latitudeObj.toString());
        } catch(NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("latitude {} is not a number", latitudeObj);
            }
            return Optional.empty();
        }
        Double longitude = null;
        try {
            longitude = Double.valueOf(longitudeObj.toString());
        } catch(NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("longitude {} is not a number", longitudeObj);
            }
            return Optional.empty();
        }
        return Optional.of(new Location(latitude, longitude));
    }
}
