package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.sql.JoinTable

@MappedEntity(value = "TBL_STUDENT", alias = "s") // <1>
data class Student(
    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.IDENTITY) // <3>
    val id: Long?,
    val name: String,
    @JoinTable(name = "TBL_STUDENT_CLASSES", alias = "sc") // <4>
    @Relation(Relation.Kind.MANY_TO_MANY) // <5>
    val classes: List<Class>,
    @field:JsonAnyGetter // <6>
    @field:JsonAnySetter
    val extras: Map<String, Any?> = emptyMap()
)
