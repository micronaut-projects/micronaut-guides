package example.micronaut

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.GeneratedValue.Type.AUTO
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

@MappedEntity // <1>
class Book(var isbn: String, var name: String) {
    @Id // <2>
    @GeneratedValue(AUTO)
    var id: Long? = null
}
