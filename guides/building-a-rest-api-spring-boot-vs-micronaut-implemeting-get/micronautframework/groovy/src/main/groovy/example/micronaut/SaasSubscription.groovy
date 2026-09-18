package example.micronaut

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

@Canonical
@CompileStatic
@Serdeable // <1>
class SaasSubscription {
    Long id
    String name
    Integer cents
}
