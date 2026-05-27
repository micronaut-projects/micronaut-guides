package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation

@MappedEntity("contact") // <1>
data class ContactEntity(
    @field:Id // <2>
    @field:GeneratedValue // <3>
    @field:Nullable // <4>
    val id: Long? = null,

    val firstName: String?,

    val lastName: String?,

    @field:Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "contact") // <5>
    val phones: List<PhoneEntity>?
)
