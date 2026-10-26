package example.micronaut

import io.micronaut.core.beans.BeanIntrospection
import spock.lang.Specification

class ContactIntrospectedSpec extends Specification { // <1>

    void "Contact is introspected"() {
        when:
        BeanIntrospection.getIntrospection(Contact) // <2>

        then:
        noExceptionThrown()
    }
}
