package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.sql.JoinTable

@MappedEntity(value = "TBL_STUDENT", alias = "s")
data class Student(
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    val id: Long?,
    val name: String,
    @JoinTable(name = "TBL_STUDENT_CLASSES", alias = "sc")
    @Relation(Relation.Kind.MANY_TO_MANY)
    val classes: List<Class>
)
