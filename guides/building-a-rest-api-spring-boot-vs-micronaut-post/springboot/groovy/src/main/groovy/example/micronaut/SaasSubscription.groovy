package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import org.springframework.data.annotation.Id

@CompileStatic
@EqualsAndHashCode
class SaasSubscription {

    @Id
    Long id
    String name
    Integer cents
}
