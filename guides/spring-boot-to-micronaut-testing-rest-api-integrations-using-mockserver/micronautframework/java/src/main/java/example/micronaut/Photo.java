package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Photo(Long id, String title, String url, String thumbnailUrl) {
}
