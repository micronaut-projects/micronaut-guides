package example.micronaut.domain;

import io.micronaut.data.annotation.Embeddable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Relation;
import io.micronaut.data.annotation.JsonSubView;
import io.micronaut.data.annotation.JsonView;
import io.micronaut.data.annotation.MappedProperty;

import java.time.LocalTime;

@Embeddable
@JsonSubView(entity = Class.class, operations = {JsonView.Operation.INSERT, JsonView.Operation.UPDATE}) // <1>
public record StudentScheduleClassSubView (
        @Id
        @MappedProperty(value = "id")
        Long classID,

        String name
) {}
