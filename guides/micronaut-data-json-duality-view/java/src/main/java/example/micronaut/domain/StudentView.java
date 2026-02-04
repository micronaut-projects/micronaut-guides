package example.micronaut.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.JsonView;
import io.micronaut.data.annotation.Relation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonView(entity = Student.class) // <1>
public class StudentView {
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    private Long id;

    private String name;

    @Relation(Relation.Kind.ONE_TO_MANY) // <4>
    private List<StudentScheduleSubView> classes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudentScheduleSubView> getClasses() {
        return classes;
    }

    public void setClasses(List<StudentScheduleSubView> classes) {
        this.classes = classes;
    }
}
