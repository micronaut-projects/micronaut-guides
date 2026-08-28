package example.micronaut

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation

@EqualsAndHashCode
@TupleConstructor
@MappedEntity("contact") // <1>
class ContactEntity {
    @Id // <2>
    @GeneratedValue // <3>
    @Nullable // <4>
    Long id

    @Nullable
    String firstName

    @Nullable
    String lastName

    @Nullable
    @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "contact") // <5>
    List<PhoneEntity> phones
}
