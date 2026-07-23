package example.micronaut.domain

import io.micronaut.data.annotation.Embeddable
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.annotation.JsonSubView

@Embeddable
@JsonSubView(entity = Class)
class StudentScheduleClassSubView {
    @Id
    @MappedProperty(value = "id")
    Long classID

    String name
}
