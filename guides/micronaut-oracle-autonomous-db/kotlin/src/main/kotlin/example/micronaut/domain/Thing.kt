package example.micronaut.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id

@MappedEntity
class Thing @Creator constructor(val name: String) {
    @Id
    @GeneratedValue
    var id: Long? = null
}
