package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

@MappedEntity("phone") // <1>
public record PhoneEntity(
        @Id // <2>
        @GeneratedValue  // <3>
        @Nullable
        Long id,

        String phone,

        @Relation(value = Relation.Kind.MANY_TO_ONE)
        ContactEntity contact
) {
}
