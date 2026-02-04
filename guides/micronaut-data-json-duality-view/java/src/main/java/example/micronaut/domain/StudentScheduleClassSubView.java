package example.micronaut.domain;

import io.micronaut.data.annotation.Embeddable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.annotation.JsonSubView;
import io.micronaut.data.annotation.JsonView;
import io.micronaut.data.annotation.MappedProperty;

import java.time.LocalTime;

@Embeddable
@JsonSubView(entity = Class.class)
public class StudentScheduleClassSubView {

    @Id
    @MappedProperty(value = "id")
    private Long classID;

    private String name;

    public Long getClassID() {
        return classID;
    }

    public void setClassID(Long classID) {
        this.classID = classID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
