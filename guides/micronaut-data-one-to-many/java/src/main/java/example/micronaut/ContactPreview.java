package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record ContactPreview(Long id, String firstName, String lastName) {
}
