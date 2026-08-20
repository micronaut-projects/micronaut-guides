package example.micronaut.domain

import io.micronaut.data.annotation.Embeddable
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.annotation.JsonSubView
import io.micronaut.data.annotation.JsonView

@Embeddable
@JsonSubView(entity = Class::class, operations = [JsonView.Operation.INSERT, JsonView.Operation.UPDATE])
data class StudentScheduleClassSubView(
    @Id
    @MappedProperty(value = "id")
    val classID: Long?,
    val name: String
)
