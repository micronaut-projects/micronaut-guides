package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import java.util.List;

@MappedEntity("contact") // <1>
public record ContactEntity(
        @Id // <2>
        @GeneratedValue // <3>
        @Nullable
        Long id,

        @Nullable
        String firstName,

        @Nullable
        String lastName,

        @Nullable
        @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "contact") // <4>
        List<PhoneEntity>phones
) {
}
