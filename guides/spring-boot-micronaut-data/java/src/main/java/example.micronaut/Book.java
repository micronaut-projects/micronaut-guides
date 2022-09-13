package example.micronaut;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public record Book(
        @GeneratedValue
        @Id
        Long id,
        String title,
        int pages
) {
}
