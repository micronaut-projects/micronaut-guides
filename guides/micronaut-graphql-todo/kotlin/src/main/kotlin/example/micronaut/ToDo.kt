package example.micronaut

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.GeneratedValue.Type.AUTO
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

@MappedEntity // <1>
class ToDo(var title: String, val authorId: Long) {

    @Id // <2>
    @GeneratedValue(AUTO)
    var id: Long? = null

    var isCompleted = false
}