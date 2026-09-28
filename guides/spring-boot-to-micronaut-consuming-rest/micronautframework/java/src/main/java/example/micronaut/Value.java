package example.micronaut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
public record Value(Long id, String quote) { }