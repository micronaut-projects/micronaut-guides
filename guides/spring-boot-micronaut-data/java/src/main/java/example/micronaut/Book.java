package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity // <1>
public record Book(
        @Id // <2>
        @GeneratedValue // <3>
        @Nullable // <4>
        Long id,
        String title,
        int pages
) {
}
