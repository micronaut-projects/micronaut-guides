package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@EqualsAndHashCode
@Serdeable // <1>
@MappedEntity // <2>
class SaasSubscription {

    @Id // <3>
    Long id
    String name
    Integer cents
}
