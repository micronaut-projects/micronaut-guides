package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@Serdeable // <1>
@MappedEntity // <2>
record SaasSubscription(@Id Long id, // <3>
                        String name,
                        Integer cents) {
}