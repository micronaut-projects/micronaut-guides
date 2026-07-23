package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.JsonView
import io.micronaut.data.annotation.Relation

@JsonView(entity = Student)
class StudentView {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Long id

    String name

    @Relation(Relation.Kind.ONE_TO_MANY)
    List<StudentScheduleSubView> classes
}
