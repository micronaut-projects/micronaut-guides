package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.JsonView
import io.micronaut.data.annotation.Relation

@JsonView(entity = Student::class)
data class StudentView(
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    val id: Long?,
    val name: String,
    @Relation(Relation.Kind.ONE_TO_MANY)
    val classes: List<StudentScheduleSubView>
)
