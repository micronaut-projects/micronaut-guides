package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
@MappedEntity("phone") // <1>
public record PhoneEntity(
        @Id // <2>
        @GeneratedValue  // <3>
        @Nullable // <4>
        Long id,

        @NonNull
        String phone,

        @Nullable
        @Relation(value = Relation.Kind.MANY_TO_ONE) // <5>
        ContactEntity contact
) {
}
