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

@MappedEntity(value = "TBL_STUDENT", alias = "s")
public record Student (
        @Id
        @GeneratedValue(GeneratedValue.Type.IDENTITY)
        Long id,
        String name,

        @JoinTable(name = "TBL_STUDENT_CLASSES", alias = "sc")
        @Relation(Relation.Kind.MANY_TO_MANY)
        List<Class> classes
) {}
