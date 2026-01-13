package example.micronaut;

import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;

/**
 *
 * @param latitude Latitude of the location
 * @param longitude Longitude of the location
 */
@Serdeable
@JsonSchema
public record Point(double latitude, double longitude) {
}
