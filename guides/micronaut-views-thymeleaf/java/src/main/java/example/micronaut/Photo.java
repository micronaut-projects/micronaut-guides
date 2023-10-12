package example.micronaut;

import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable // <1>
@ReflectiveAccess // <2>
public record Photo(Long albumId, String title, String url, String thumbnailUrl) {
}
