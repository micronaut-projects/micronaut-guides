package example.micronaut.domain;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.JsonView;
import io.micronaut.data.annotation.Relation;
import java.util.List;

@JsonView(entity = Student.class)
public class StudentView {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    private Long id;

    private String name;

    @Relation(Relation.Kind.ONE_TO_MANY)
    private List<ClassSubView> classes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}