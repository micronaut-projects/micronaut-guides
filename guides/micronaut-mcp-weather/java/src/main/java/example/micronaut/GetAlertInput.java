package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.jsonschema.JsonSchema;

/**
 *
 * @param state Two-letter US state code (e.g. CA, NY)
 */
@Introspected // <1>
@JsonSchema // <2>
public record GetAlertInput(String state) {
}
