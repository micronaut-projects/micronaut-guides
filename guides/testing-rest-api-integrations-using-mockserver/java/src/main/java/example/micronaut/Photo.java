package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable // <1>
public record Photo(Long id, String title, String url, String thumbnailUrl) {
}
