package example.micronaut.domain;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.annotation.sql.JoinTable;
import java.util.List;

@MappedEntity(value = "TBL_STUDENT", alias = "s")
public class Student {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;

    private String name;

    @JoinTable(name = "TBL_STUDENT_CLASSES", alias = "sc")
    @Relation(Relation.Kind.MANY_TO_MANY)
    private List<Class> classes;
}
