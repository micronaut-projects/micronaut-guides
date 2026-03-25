package example.micronaut.domain;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.Relation;

@MappedEntity(value = "TBL_STUDENT_CLASSES", alias = "sc") // <1>
public record StudentClass (
        @Id // <2>
        @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
        Long id,
        @Relation(Relation.Kind.MANY_TO_ONE) // <4>
        Student student,
        @Relation(Relation.Kind.MANY_TO_ONE) // <4>
        @MappedProperty("CLASS_ID") // <5>
        Class clazz
) {}
