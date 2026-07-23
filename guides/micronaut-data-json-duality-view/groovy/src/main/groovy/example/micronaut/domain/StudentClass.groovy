package example.micronaut.domain

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.annotation.Relation

@MappedEntity(value = "TBL_STUDENT_CLASSES", alias = "sc")
class StudentClass {
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    Long id

    @Relation(Relation.Kind.MANY_TO_ONE)
    Student student

    @Relation(Relation.Kind.MANY_TO_ONE)
    @MappedProperty("CLASS_ID")
    Class clazz
}
