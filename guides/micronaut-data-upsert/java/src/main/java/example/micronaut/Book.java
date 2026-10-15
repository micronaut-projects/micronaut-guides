package example.micronaut;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import org.jspecify.annotations.Nullable;

@Serdeable // <1>
@MappedEntity // <2>
public record Book(
        @Id @Nullable Long id, // <3> <4>
        String title,
        @Nullable Integer pages) {
}
