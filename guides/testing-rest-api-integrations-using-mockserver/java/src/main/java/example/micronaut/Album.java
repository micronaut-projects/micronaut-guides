package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable // <1>
public record Album(Long albumId, List<Photo> photos) {
}
