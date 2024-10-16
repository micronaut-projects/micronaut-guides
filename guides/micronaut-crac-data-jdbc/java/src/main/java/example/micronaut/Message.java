package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity // <1>
public record Message(@Nullable @Id @GeneratedValue Long id, // <2>
                      String message) {
}
