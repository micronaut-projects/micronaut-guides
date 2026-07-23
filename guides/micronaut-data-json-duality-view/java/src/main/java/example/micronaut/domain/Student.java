package example.micronaut.domain;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.annotation.sql.JoinTable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@MappedEntity(value = "TBL_STUDENT", alias = "s") // <1>
public record Student (
        @Id // <2>
        @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
        Long id,
        String name,

        @JoinTable(name = "TBL_STUDENT_CLASSES", alias = "sc") // <4>
        @Relation(Relation.Kind.MANY_TO_MANY) // <5>
        List<Class> classes
) {}
