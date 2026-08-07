package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.JsonSubView
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@JsonSubView(entity = StudentClass::class)
data class StudentScheduleSubView(
    @Id
    val id: Long,
    @JsonProperty("class")
    @Relation(Relation.Kind.ONE_TO_ONE)
    val clazz: StudentScheduleClassSubView
)
