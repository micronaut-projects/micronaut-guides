package example.micronaut.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.JsonSubView;
import io.micronaut.data.annotation.Relation;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@JsonSubView(entity = StudentClass.class)
public record StudentScheduleSubView (
        @Id
        Long id,

        @JsonProperty("class")
        @Relation(Relation.Kind.ONE_TO_ONE)
        StudentScheduleClassSubView clazz
) {}
