package example.micronaut;

import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;

/**
 *
 * @param latitude Latitude of the location
 * @param longitude Longitude of the location
 */
@Serdeable // <1>
@JsonSchema // <2>
public record Point(double latitude, double longitude) {
}
