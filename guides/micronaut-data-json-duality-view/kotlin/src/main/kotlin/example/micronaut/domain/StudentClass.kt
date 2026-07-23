package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.annotation.Relation

@MappedEntity(value = "TBL_STUDENT_CLASSES", alias = "sc")
data class StudentClass(
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    val id: Long?,
    @Relation(Relation.Kind.MANY_TO_ONE)
    val student: Student,
    @Relation(Relation.Kind.MANY_TO_ONE)
    @MappedProperty("CLASS_ID")
    val clazz: Class
)
