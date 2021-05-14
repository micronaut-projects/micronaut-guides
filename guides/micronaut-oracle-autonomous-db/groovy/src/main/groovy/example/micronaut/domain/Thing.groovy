package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Creator
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

@MappedEntity
@CompileStatic
class Thing {

    @Id
    @GeneratedValue
    Long id

    final String name

    @Creator
    Thing(String name) {
        this.name = name
    }
}
