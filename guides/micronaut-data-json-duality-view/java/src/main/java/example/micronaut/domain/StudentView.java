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
public record StudentView (
        @Id // <2>
        @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
        Long id,

        String name,

        @Relation(Relation.Kind.ONE_TO_MANY) // <4>
        List<StudentScheduleSubView> classes
) {}
