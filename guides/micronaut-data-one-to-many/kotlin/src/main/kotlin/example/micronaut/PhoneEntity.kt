package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation

@MappedEntity("phone") // <1>
data class PhoneEntity(
    @field:Id // <2>
    @field:GeneratedValue // <3>
    @field:Nullable // <4>
    val id: Long? = null,

    val phone: String,

    @field:Relation(value = Relation.Kind.MANY_TO_ONE) // <5>
    val contact: ContactEntity?
)
