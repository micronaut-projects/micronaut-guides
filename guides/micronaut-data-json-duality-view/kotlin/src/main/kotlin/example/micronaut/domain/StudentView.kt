package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.JsonView
import io.micronaut.data.annotation.Relation

@JsonView(entity = Student::class) // <1>
data class StudentView(
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    val id: Long?,
    val name: String,
    @Relation(Relation.Kind.ONE_TO_MANY) // <4>
    val classes: List<StudentScheduleSubView>,
    @field:JsonAnyGetter // <5>
    @field:JsonAnySetter
    val extras: Map<String, Any?> = emptyMap()
)
