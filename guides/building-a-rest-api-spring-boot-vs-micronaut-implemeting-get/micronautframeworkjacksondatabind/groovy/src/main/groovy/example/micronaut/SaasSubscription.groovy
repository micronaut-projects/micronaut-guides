package example.micronaut

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@Canonical
@CompileStatic
@JsonPropertyOrder(["id", "name", "cents"])
class SaasSubscription {
    Long id
    String name
    Integer cents
}
